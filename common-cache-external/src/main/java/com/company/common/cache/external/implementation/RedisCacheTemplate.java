package com.company.common.cache.external.implementation;

import com.company.common.cache.external.port.ExternalCacheTemplate;
import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
        value = {"app.cache.external.enable"},
        havingValue = "true"
)
@Component
public class RedisCacheTemplate implements ExternalCacheTemplate {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheTemplate.class);

    RedisConnection redisConnection;
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisCacheConfigurationProperties props;

    private String keyGen(String cacheName, Object key) {
        return cacheName.equals("") ? key.toString() : this.props.getApplicationShortName() + this.props.getDelimiter() + cacheName + this.props.getDelimiter() + key;
    }

    public <T> T getObject(String key) {
        return this.getObject("", key);
    }

    public <T> T getObject(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis GET: key = {}", keyGen);
        return (T) redisTemplate.opsForValue().get(keyGen);
    }

    public void putObject(String cacheName, String key, Object value) {
        String keyGen = this.keyGen(cacheName, key);
        long expireSeconds = getExpireSecondsConfig(cacheName);
        log.info("Redis SET: key = {}, expire = {} seconds", keyGen, expireSeconds);
        this.redisTemplate.opsForValue().set(keyGen, value, Duration.ofSeconds(expireSeconds));
    }

    public void putObject(String key, Object value) {
        this.putObject("", key, value);
    }

    public void putObject(String cacheName, String key, Object value, Duration duration) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis SET: key = {}, expire = {} seconds", keyGen, duration.getSeconds());
        this.redisTemplate.opsForValue().set(keyGen, value, duration);
    }

    public void putObject(String key, Object value, Duration duration) {
        this.putObject("", key, value, duration);
    }

    public boolean hasKey(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("Redis EXISTS: key = {}", keyGen);
        Boolean result = this.redisTemplate.hasKey(keyGen);
        return result != null && result;
    }

    public boolean hasKey(String key) {
        return this.hasKey("", key);
    }

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

    public void invalidate(String key) {
        this.invalidate("", key);
    }

    public Set<String> keySet(String keyPattern) {
        log.info("Redis SCAN: count = {}, match keyPattern = {} ", props.getScanLimit(), keyPattern);
        Set<String> result = props.isCluster() ? this.keySetCluster(keyPattern) : this.keySetStandalone(keyPattern);
        log.info("Total key match: {}", result.size());
        return result;
    }

    private long getExpireSecondsConfig(String cacheName) {
        if (!CollectionUtils.isEmpty(props.getCacheExpirations()) && props.getCacheExpirations().get(cacheName) != null) {
            return props.getCacheExpirations().get(cacheName);
        } else if (props.getCacheDefaultExpiration() != null) {
            return props.getCacheDefaultExpiration();
        }

        return -1;
    }

    private Set<String> keySetStandalone(String keyPattern) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(props.getScanLimit()).build();
        Cursor<byte[]> cursor = this.redisConnection.scan(options);

        while(cursor.hasNext()) {
            String key = new String(cursor.next());
            keys.add(key);
        }

        return keys;
    }

    private Set<String> keySetCluster(String keyPattern) {
        LettuceClusterConnection con = (LettuceClusterConnection) redisConnectionFactory.getClusterConnection();
        RedisAdvancedClusterAsyncCommands<byte[], byte[]> nativeConnection = (RedisAdvancedClusterAsyncCommands) con.getNativeConnection();
        RedisAdvancedClusterCommands<byte[], byte[]> sync = nativeConnection.getStatefulConnection().sync();
        KeyScanCursor<byte[]> scanCursor = null;
        ScanArgs scanArgs = new ScanArgs();
        scanArgs.match(keyPattern);
        scanArgs.limit(props.getScanLimit());
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
