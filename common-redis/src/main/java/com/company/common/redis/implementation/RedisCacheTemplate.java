package com.company.common.redis.implementation;

import com.company.common.redis.port.CacheTemplate;
import com.company.common.redis.properties.CacheConfigurationProperties;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ConditionalOnProperty(
        value = {"spring.redis.enabled"},
        havingValue = "true"
)
@Component
public class RedisCacheTemplate implements CacheTemplate {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheTemplate.class);

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired(required = false)
    private CacheConfigurationProperties props;

    private String keyGen(String cacheName, Object key) {
        return cacheName.equals("") ? key.toString() : this.props.getRedis().getKeyPrefix() + this.props.getDelimiter() + cacheName + this.props.getDelimiter() + key;
    }

    @Override
    public Object getObject(String key) {
        return this.getObject("", key);
    }

    @Override
    public Object getObject(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis GET: key = {}", keyGen);
        return redisTemplate.opsForValue().get(keyGen);
    }

    @Override
    public void putObject(String cacheName, String key, Object value) {
        String keyGen = this.keyGen(cacheName, key);
        Duration timeToLive = this.getTimeToLiveConfig(cacheName);
        if (timeToLive != null) {
            log.info("Redis SET: key = {}, expire = {} seconds", keyGen, timeToLive.toSeconds());
            this.redisTemplate.opsForValue().set(keyGen, value, timeToLive);
        } else {
            log.info("Redis SET: key = {}, no set expire", keyGen);
            this.redisTemplate.opsForValue().set(keyGen, value);
        }
    }

    @Override
    public void putObject(String key, Object value) {
        this.putObject("", key, value);
    }

    @Override
    public void putObject(String cacheName, String key, Object value, Duration duration) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis SET: key = {}, expire = {} seconds", keyGen, duration.toSeconds());
        this.redisTemplate.opsForValue().set(keyGen, value, duration);
    }

    @Override
    public void putObject(String key, Object value, Duration duration) {
        this.putObject("", key, value, duration);
    }

    @Override
    public boolean hasKey(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis EXISTS: key = {}", keyGen);
        Boolean result = this.redisTemplate.hasKey(keyGen);
        return result != null && result;
    }

    @Override
    public boolean hasKey(String key) {
        return this.hasKey("", key);
    }

    @Override
    public void invalidate(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis DEL: key = {}", keyGen);
        Boolean result = redisTemplate.delete(keyGen);

        if (Boolean.TRUE.equals(result)) {
            log.info("invalidate key = {} successful", keyGen);
        } else {
            log.warn("invalidate key = {} failed !", keyGen);
        }
    }

    @Override
    public void invalidate(String key) {
        this.invalidate("", key);
    }

    @Override
    public Set<String> keySet(String keyPattern, long count) {
        log.info("Redis SCAN: count = {}, match keyPattern = {} ", count, keyPattern);
        Set<String> result = this.keySetStandalone(keyPattern, count);
        log.info("Total key match: {}", result.size());
        return result;
    }

    private Duration getTimeToLiveConfig(String cacheName) {
        if (!CollectionUtils.isEmpty(props.getCustomCache()) && props.getCustomCache().containsKey(cacheName)) {
            return props.getCustomCache().get(cacheName).getTimeToLive();
        } else if (props.getRedis().getTimeToLive() != null) {
            return props.getRedis().getTimeToLive();
        }

        return null;
    }

    private Set<String> keySetStandalone(String keyPattern, long count) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(count).build();

        try (Cursor<String> cursor = this.redisTemplate.scan(options)) {
            while(cursor.hasNext()) {
                keys.add(cursor.next());
            }
        } catch (Exception e) {
            log.warn("Exception when scanning: {}", e.getMessage());
        }

        return keys;
    }

    private Set<String> keySetCluster(String keyPattern) {
        LettuceClusterConnection con = (LettuceClusterConnection) null;
        RedisAdvancedClusterAsyncCommands<byte[], byte[]> nativeConnection = (RedisAdvancedClusterAsyncCommands) con.getNativeConnection();
        RedisAdvancedClusterCommands<byte[], byte[]> sync = nativeConnection.getStatefulConnection().sync();
        KeyScanCursor<byte[]> scanCursor = null;
        ScanArgs scanArgs = new ScanArgs();
        scanArgs.match(keyPattern);
        scanArgs.limit(1000);
        Set<byte[]> keys = new HashSet<>();

        do {
            if (scanCursor == null) {
                scanCursor = sync.scan(scanArgs);
            } else {
                scanCursor = sync.scan(scanCursor, scanArgs);
            }

            keys.addAll(scanCursor.getKeys());
        } while(!scanCursor.isFinished());

        return keys.stream().map(String::new).collect(Collectors.toSet());
    }
}
