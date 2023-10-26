package com.company.common.redis.configuration;

import java.util.Map;
import java.util.stream.Collectors;

import com.company.common.redis.properties.CacheConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.StringUtils;

@ConditionalOnProperty(
        value = {"spring.redis.enabled"},
        havingValue = "true"
)
@EnableCaching
@Configuration
public class RedisCacheManagerConfig  {

    private final CacheConfigurationProperties props;
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisCacheManagerConfig(CacheConfigurationProperties props, RedisConnectionFactory redisConnectionFactory) {
        this.props = props;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromCacheWriter(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)
        ).cacheDefaults(this.getDefaultRedisCacheConfiguration());

        builder.withInitialCacheConfigurations(this.getCustomRedisCacheConfigurations());
        return builder.build();
    }

    private RedisCacheConfiguration getDefaultRedisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
        ).computePrefixWith(cacheName -> {
            String prefix = props.getRedis().getKeyPrefix() + props.getDelimiter();
            if (StringUtils.hasLength(cacheName)) {
                prefix = prefix + cacheName + props.getDelimiter();
            }

            return prefix;
        }).entryTtl(props.getRedis().getTimeToLive());
    }

    private Map<String, RedisCacheConfiguration> getCustomRedisCacheConfigurations() {
        return props.getCustomCache().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> this.getDefaultRedisCacheConfiguration().entryTtl(entry.getValue().getTimeToLive())
        ));
    }
}
