package com.company.common.cache.internal.configuration;

import com.company.common.cache.internal.implementation.FullKeyGenerator;
import com.company.common.cache.internal.properties.CaffeineCacheConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineKeyGeneratorConfig {
    CaffeineCacheConfigurationProperties properties;

    @Autowired
    public CaffeineKeyGeneratorConfig(CaffeineCacheConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KeyGenerator caffeineKeyGenerator() {
        return new FullKeyGenerator(this.properties);
    }
}
