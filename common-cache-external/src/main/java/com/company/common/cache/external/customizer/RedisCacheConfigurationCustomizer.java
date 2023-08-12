package com.company.common.cache.external.customizer;

import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

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
        return this.redisCacheConfigurationProperties.getCacheExpirations().entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> this.getDefaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds(entry.getValue()))));
    }
}
