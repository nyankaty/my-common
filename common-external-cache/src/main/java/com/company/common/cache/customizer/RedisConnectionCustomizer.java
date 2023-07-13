package com.company.common.cache.customizer;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class RedisConnectionCustomizer {
    RedisConnectionFactory redisConnectionFactory;

    public RedisConnectionCustomizer(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public RedisConnection getRedisConnection() {
        return this.redisConnectionFactory.getConnection();
    }
}
