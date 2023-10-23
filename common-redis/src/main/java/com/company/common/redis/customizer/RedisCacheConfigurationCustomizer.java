package com.company.common.redis.customizer;

import com.company.common.redis.properties.RedisCacheConfigurationProperties;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.StringUtils;

public class RedisCacheConfigurationCustomizer {
    RedisCacheConfigurationProperties props;

    public RedisCacheConfigurationCustomizer(RedisCacheConfigurationProperties props) {
        this.props = props;
    }

    public RedisCacheConfiguration getDefaultRedisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
        ).computePrefixWith(cacheName -> {
            String prefix = props.getApplicationShortName() + props.getDelimiter();
            if (StringUtils.hasLength(cacheName)) {
                prefix = prefix + cacheName + props.getDelimiter();
            }

            return prefix;
        }).entryTtl(Duration.ofSeconds(props.getCacheDefaultExpiration()));
    }

    public Map<String, RedisCacheConfiguration> getRedisCacheConfigurations() {
        return props.getCacheExpirations().entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> this.getDefaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds(entry.getValue()))));
    }
}
