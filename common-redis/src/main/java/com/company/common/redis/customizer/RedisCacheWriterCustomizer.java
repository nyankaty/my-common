package com.company.common.redis.customizer;

import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class RedisCacheWriterCustomizer {
    RedisConnectionFactory redisConnectionFactory;

    public RedisCacheWriterCustomizer(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public RedisCacheWriter getRedisCacheWriter(boolean lock) {
        return lock ? RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory) : RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
    }
}
