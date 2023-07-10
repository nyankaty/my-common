package com.company.common.cache.implementation;

import com.company.common.cache.customizer.RedisConnectionCustomizer;
import com.company.common.cache.customizer.RedisConnectionFactoryCustomizer;
import com.company.common.cache.port.ExternalCacheTemplate;
import com.company.common.cache.properties.RedisCacheConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@ComponentScan({"com.company.common.cache.properties"})
public class RedisCacheTemplate implements ExternalCacheTemplate {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheTemplate.class);
    RedisConnection redisConnection;
    RedisTemplate<String, String> redisTemplate;
    RedisCacheConfigurationProperties externalCacheProp;
    private static final ObjectMapper om = new ObjectMapper();
    static final String DEFAULT_CACHE_NAME = "";

    public RedisCacheTemplate(RedisCacheConfigurationProperties externalCacheProp) {
        RedisConnectionFactory redisConnectionFactory = (new RedisConnectionFactoryCustomizer(externalCacheProp)).getRedisConnectionFactory();
        this.redisConnection = (new RedisConnectionCustomizer(redisConnectionFactory)).getRedisConnection();
        this.redisTemplate = new RedisTemplate();
        this.redisTemplate.setConnectionFactory(redisConnectionFactory);
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        this.redisTemplate.afterPropertiesSet();
        this.externalCacheProp = externalCacheProp;
    }

    private String keyGen(String cacheName, Object key) {
        return cacheName.equals("") ? key.toString() : this.externalCacheProp.getApplicationShortName() + this.externalCacheProp.getDelimiter() + cacheName + this.externalCacheProp.getDelimiter() + key;
    }

    public <T> T getObject(String cacheName, String key, Class<T> objectClass) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate get: cacheName = {}, key = {}", cacheName, keyGen);
        String valueStr = (String)this.redisTemplate.opsForValue().get(keyGen);
        return this.readObjectFromValueStr(valueStr, keyGen, objectClass);
    }

    public <T> T getObject(String key, Class<T> objectClass) {
        return this.getObject("", key, objectClass);
    }

    public <T> List<T> getObjectAsList(String cacheName, String key, Class<T> objectClass) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate get: cacheName = {}, key = {}", cacheName, keyGen);
        String valueStr = (String)this.redisTemplate.opsForValue().get(keyGen);
        if (valueStr == null) {
            log.info("Key {} does not exist", keyGen);
            return null;
        } else {
            CollectionType listType = om.getTypeFactory().constructCollectionType(ArrayList.class, objectClass);
            om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            try {
                List<T> objectList = (List)om.readValue(valueStr, listType);
                return objectList;
            } catch (JsonProcessingException var9) {
                var9.printStackTrace();
                log.info("Invalid value format, value: {}", valueStr);
                return null;
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
                T object = om.readValue(valueStr, objectClass);
                return object;
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
        String valueStr = (String)this.redisTemplate.opsForList().leftPop(keyGen);
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
        if (this.externalCacheProp.getCacheExpirations().get(cacheName) != null && !this.externalCacheProp.getCacheExpirations().isEmpty()) {
            this.redisConnection.expire(keyGen.getBytes(StandardCharsets.UTF_8), (Long)this.externalCacheProp.getCacheExpirations().get(cacheName));
        } else if (this.externalCacheProp.getCacheDefaultExpiration() != null) {
            this.redisConnection.expire(keyGen.getBytes(StandardCharsets.UTF_8), this.externalCacheProp.getCacheDefaultExpiration());
        }

        return valueStr;
    }

    public <T> String putObject(String cacheName, String key, T value) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate put: cacheName = {}, key = {}, value = {}", new Object[]{cacheName, keyGen, value});
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
        log.info("RedisCacheTemplate put: cacheName = {}, key = {}, value = {}", new Object[]{cacheName, keyGen, valueList});

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
        log.info("RedisCacheTemplate putObjectToList: cacheName = {}, key = {}, value = {}", new Object[]{cacheName, keyGen, value});
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
        return p == null ? false : p;
    }

    public boolean hasKey(String key) {
        return this.hasKey("", key);
    }

    public void invalidate(String cacheName, String key) {
        String keyGen = this.keyGen(cacheName, key);
        log.info("RedisCacheTemplate invalidate: cacheName = {}, key = {}", cacheName, keyGen);
        this.redisConnection.del(new byte[][]{keyGen.getBytes(StandardCharsets.UTF_8)});
    }

    public void invalidate(String key) {
        this.invalidate("", key);
    }

    private Set<String> keySetStandalone(String keyPattern, long count) {
        Set<String> set = new HashSet();
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(count).build();
        Cursor<byte[]> c = this.redisConnection.scan(options);
        log.info("RedisCacheTemplate keySet({}, {}) = {", keyPattern, count);

        while(c.hasNext()) {
            String key = new String((byte[])c.next());
            set.add(key);
            log.info(key);
        }

        log.info("}");
        return set;
    }

    private LettuceConnectionFactory getLettuceConnectionFactory() {
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder().build();
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(this.externalCacheProp.getNodes());
        if (!StringUtils.isEmpty(this.externalCacheProp.getPassword())) {
            redisClusterConfiguration.setPassword(RedisPassword.of(this.externalCacheProp.getPassword()));
        }

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private Set<String> keySetCluster(String keyPattern) {
        LettuceConnectionFactory lettuceConnectionFactory = this.getLettuceConnectionFactory();
        LettuceClusterConnection con = (LettuceClusterConnection)lettuceConnectionFactory.getClusterConnection();
        RedisAdvancedClusterAsyncCommands<byte[], byte[]> nativeConnection = (RedisAdvancedClusterAsyncCommands)con.getNativeConnection();
        RedisAdvancedClusterCommands<byte[], byte[]> sync = nativeConnection.getStatefulConnection().sync();
        KeyScanCursor<byte[]> scanCursor = null;
        ScanArgs scanArgs = new ScanArgs();
        scanArgs.match(keyPattern);
        Set<byte[]> setByte = new HashSet();
        HashSet setString = new HashSet();

        do {
            if (scanCursor == null) {
                scanCursor = sync.scan(scanArgs);
            } else {
                scanCursor = sync.scan(scanCursor, scanArgs);
            }

            setByte.addAll(scanCursor.getKeys());
        } while(!scanCursor.isFinished());

        setByte.forEach((bytes) -> {
            String s = new String(bytes);
            setString.add(s);
        });
        return setString;
    }

    public Set<String> keySet(String keyPattern, long count) {
        return !CollectionUtils.isEmpty(this.externalCacheProp.getNodes()) ? this.keySetCluster(keyPattern) : this.keySetStandalone(keyPattern, count);
    }
}
