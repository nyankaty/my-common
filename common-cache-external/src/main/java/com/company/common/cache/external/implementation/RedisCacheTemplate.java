package com.company.common.cache.external.implementation;


import com.company.common.cache.external.port.ExternalCacheTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;



    private String keyGen(String cacheName, Object key) {
        KeyGen f = new KeyGen();
        KeyGen.generate(null, null, null)
        return cacheName.equals("") ? key.toString() : this.externalCacheProp.getApplicationShortName() + this.externalCacheProp.getDelimiter() + cacheName + this.externalCacheProp.getDelimiter() + key;
    }

    @Cacheable(key = "1", keyGenerator = "sg")
    public <T> T getObject(String key) {
        log.info("RedisCacheTemplate get: key = {}", key);
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    public <T> List<T> getObjectAsList(String cacheName, String key, Class<T> objectClass) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate get: cacheName = {}, key = {}", cacheName, keyGen);
        String valueStr = this.redisTemplate.opsForValue().get(keyGen);
        if (valueStr == null) {
            log.info("Key {} does not exist", keyGen);
            return Collections.emptyList();
        } else {
            CollectionType listType = om.getTypeFactory().constructCollectionType(ArrayList.class, objectClass);
            om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            try {
                return om.readValue(valueStr, listType);
            } catch (JsonProcessingException var9) {
                var9.printStackTrace();
                log.info("Invalid value format, value: {}", valueStr);
                return Collections.emptyList();
            }
        }
    }

    private <T> T readObjectFromValueStr(String valueStr, String keyGen, Class<T> objectClass) {
        if (valueStr == null) {
            log.info("Key {} does not exist", keyGen);
            return null;
        } else if (objectClass == String.class) {
            log.info("String value format, value: {}", valueStr);
            return (T) valueStr;
        } else {
            try {
                return om.readValue(valueStr, objectClass);
            } catch (JsonProcessingException var6) {
                var6.printStackTrace();
                log.info("Invalid value format, value: {}", valueStr);
                return null;
            }
        }
    }

    public <T> T getObjectFromList(String cacheName, String key, Class<T> objectClass) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate get: cacheName = {}, key = {}", cacheName, keyGen);
        String valueStr = this.redisTemplate.opsForList().leftPop(keyGen);
        return this.readObjectFromValueStr(valueStr, keyGen, objectClass);
    }

    public <T> T getObjectFromList(String key, Class<T> objectClass) {
        return this.getObjectFromList("", key, objectClass);
    }

    public <T> List<T> getObjectAsList(String key, Class<T> objectClass) {
        return this.getObjectAsList("", key, objectClass);
    }

    private String putValueStr(String cacheName, String keyGen, String valueStr) {
        this.redisTemplate.opsForValue().set(keyGen, valueStr);
        if (this.expirationsProp.getCacheExpirations().get(cacheName) != null && !this.expirationsProp.getCacheExpirations().isEmpty()) {
            this.redisConnection.expire(keyGen.getBytes(StandardCharsets.UTF_8), this.expirationsProp.getCacheExpirations().get(cacheName));
        } else if (this.externalCacheProp.getCacheDefaultExpiration() != null) {
            this.redisConnection.expire(keyGen.getBytes(StandardCharsets.UTF_8), this.externalCacheProp.getCacheDefaultExpiration());
        }

        return valueStr;
    }

    public <T> String putObject(String cacheName, String key, T value) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate put: cacheName = {}, key = {}, value = {}", cacheName, keyGen, value);
        String valueStr;
        if (value instanceof String) {
            log.info("Put value as String, value: {}", value);
            valueStr = (String)value;
        } else {
            try {
                valueStr = om.writeValueAsString(value);
            } catch (JsonProcessingException var7) {
                var7.printStackTrace();
                log.info("Invalid value format, value: {}", value);
                return null;
            }
        }

        return this.putValueStr(cacheName, keyGen, valueStr);
    }

    public <T> String putObject(String key, T value) {
        return this.putObject("", key, value);
    }

    public <T> String putObjectAsList(String cacheName, String key, List<T> valueList) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate put: cacheName = {}, key = {}, value = {}", cacheName, keyGen, valueList);

        String valueStr;
        try {
            valueStr = om.writeValueAsString(valueList);
        } catch (JsonProcessingException var7) {
            var7.printStackTrace();
            log.info("Invalid value format, value: {}", valueList);
            return null;
        }

        return this.putValueStr(cacheName, keyGen, valueStr);
    }

    public <T> String putObjectAsList(String key, List<T> valueList) {
        return this.putObjectAsList("", key, valueList);
    }

    public <T> String putObjectToList(String cacheName, String key, T value) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate putObjectToList: cacheName = {}, key = {}, value = {}", cacheName, keyGen, value);
        String valueStr;
        if (value instanceof String) {
            valueStr = (String)value;
        } else {
            try {
                valueStr = om.writeValueAsString(value);
            } catch (JsonProcessingException var7) {
                var7.printStackTrace();
                log.info("Invalid value format, value: {}", value);
                return null;
            }
        }

        this.redisTemplate.opsForList().leftPush(keyGen, valueStr);
        return valueStr;
    }

    public <T> String putObjectToList(String key, T value) {
        return this.putObjectToList("", key, value);
    }

    public boolean hasKey(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        Boolean p = this.redisTemplate.hasKey(keyGen);
        return p != null && p;
    }

    public boolean hasKey(String key) {
        return this.hasKey("", key);
    }

    public void invalidate(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate invalidate: cacheName = {}, key = {}", cacheName, keyGen);
        this.redisConnection.del(keyGen.getBytes(StandardCharsets.UTF_8));
    }

    public void invalidate(String key) {
        this.invalidate("", key);
    }

    private Set<String> keySetStandalone(String keyPattern, long count) {
        Set<String> set = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(count).build();
        Cursor<byte[]> c = this.redisConnection.scan(options);
        log.info("RedisCacheTemplate keySet({}, {}) = {", keyPattern, count);

        while(c.hasNext()) {
            String key = new String(c.next());
            set.add(key);
            log.info(key);
        }

        log.info("}");
        return set;
    }

    private Set<String> keySetCluster(String keyPattern) {
        LettuceConnectionFactory lettuceConnectionFactory = null;
        LettuceClusterConnection con = (LettuceClusterConnection) lettuceConnectionFactory.getClusterConnection();
        RedisAdvancedClusterAsyncCommands<byte[], byte[]> nativeConnection = (RedisAdvancedClusterAsyncCommands) con.getNativeConnection();
        RedisAdvancedClusterCommands<byte[], byte[]> sync = nativeConnection.getStatefulConnection().sync();
        KeyScanCursor<byte[]> scanCursor = null;
        ScanArgs scanArgs = new ScanArgs();
        scanArgs.match(keyPattern);
        Set<byte[]> setByte = new HashSet<>();

        do {
            if (scanCursor == null) {
                scanCursor = sync.scan(scanArgs);
            } else {
                scanCursor = sync.scan(scanCursor, scanArgs);
            }

            setByte.addAll(scanCursor.getKeys());
        } while(!scanCursor.isFinished());

        return setByte.stream().map(String::new).collect(Collectors.toSet());
    }

    public Set<String> keySet(String keyPattern, long count) {
        return !CollectionUtils.isEmpty(this.externalCacheProp.getNodes()) ? this.keySetCluster(keyPattern) : this.keySetStandalone(keyPattern, count);
    }
}
