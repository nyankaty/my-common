package com.company.common.cache.external.customizer;

import com.company.common.cache.external.properties.CustomCacheExpirationsProperties;
import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.StringUtils;

public class RedisCacheConfigurationCustomizer {
    RedisCacheConfigurationProperties properties;
    CustomCacheExpirationsProperties expirationsProperties;

    public RedisCacheConfigurationCustomizer(RedisCacheConfigurationProperties properties, CustomCacheExpirationsProperties expirationsProperties) {
        this.properties = properties;
        this.expirationsProperties = expirationsProperties;
    }

    public RedisCacheConfiguration getDefaultRedisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
        ).computePrefixWith(cacheName -> {
            String prefix = this.properties.getApplicationShortName() + this.properties.getDelimiter();
            if (StringUtils.hasLength(cacheName)) {
                prefix = prefix + cacheName + this.properties.getDelimiter();
            }

            return prefix;
        }).entryTtl(Duration.ofSeconds(this.properties.getCacheDefaultExpiration()));
    }

    public Map<String, RedisCacheConfiguration> getRedisCacheConfigurations() {
        return this.expirationsProperties.getCacheExpirations().entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> this.getDefaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds(entry.getValue()))));
    }
}
