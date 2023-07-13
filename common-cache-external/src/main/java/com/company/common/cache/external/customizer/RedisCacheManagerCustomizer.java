package com.company.common.cache.external.customizer;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

public class RedisCacheManagerCustomizer {
    RedisCacheConfiguration defaultRedisCacheConfiguration;
    Map<String, RedisCacheConfiguration> redisCacheConfigurations;
    RedisCacheWriter redisCacheWriter;

    public RedisCacheManagerCustomizer(RedisCacheConfiguration defaultRedisCacheConfiguration, Map<String, RedisCacheConfiguration> redisCacheConfigurations, RedisCacheWriter redisCacheWriter) {
        this.defaultRedisCacheConfiguration = defaultRedisCacheConfiguration;
        this.redisCacheConfigurations = redisCacheConfigurations;
        this.redisCacheWriter = redisCacheWriter;
    }

    public RedisCacheManager getRedisCacheManager() {
        return new RedisCacheManager(this.redisCacheWriter, this.defaultRedisCacheConfiguration, this.redisCacheConfigurations);
    }
}
