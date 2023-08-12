package com.company.common.cache.internal.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        value = {"app.cache.internal.enable"},
        havingValue = "true"
)
@Configuration
@ConfigurationPropertiesScan({"com.company.common.cache.internal.properties"})
public class CaffeineCacheConfig {
    Caffeine<Object, Object> caffeine;

    @Autowired
    public CaffeineCacheConfig(Caffeine<Object, Object> caffeine) {
        this.caffeine = caffeine;
    }

    @Bean
    public Cache<Object, Object> caffeineCache() {
        return this.caffeine.build();
    }
}
