package com.company.common.cache.customizer;

import com.company.common.cache.properties.RedisCacheConfigurationProperties;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.util.StringUtils;


public class RedisCacheConfigurationCustomizer {
    RedisCacheConfigurationProperties redisCacheConfigurationProperties;

    public RedisCacheConfigurationCustomizer(RedisCacheConfigurationProperties redisCacheConfigurationProperties) {
        this.redisCacheConfigurationProperties = redisCacheConfigurationProperties;
    }

    public RedisCacheConfiguration getDefaultRedisCacheConfiguration() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        SerializationPair<Object> pair = SerializationPair.fromSerializer(jdkSerializer);
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair).computePrefixWith(cacheName -> {
            String prefix = this.redisCacheConfigurationProperties.getApplicationShortName() + this.redisCacheConfigurationProperties.getDelimiter();
            if (!StringUtils.isEmpty(cacheName)) {
                prefix = prefix + cacheName + this.redisCacheConfigurationProperties.getDelimiter();
            }

            return prefix;
        }).entryTtl(Duration.ofSeconds(this.redisCacheConfigurationProperties.getCacheDefaultExpiration()));
    }

    public Map<String, RedisCacheConfiguration> getRedisCacheConfigurations() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap();
        Iterator var4 = this.redisCacheConfigurationProperties.getCacheExpirations().entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, Long> cacheNameAndTimeout = (Map.Entry)var4.next();
            cacheConfigurations.put(cacheNameAndTimeout.getKey(), this.getDefaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds((Long)cacheNameAndTimeout.getValue())));
        }

        return cacheConfigurations;
    }
}
