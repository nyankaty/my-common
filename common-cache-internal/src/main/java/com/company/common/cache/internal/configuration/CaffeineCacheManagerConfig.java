package com.company.common.cache.internal.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConditionalOnProperty(
        value = {"app.cache.internal.enable"},
        havingValue = "true"
)
@EnableCaching
@Configuration
@ComponentScan({"com.company.common"})
@ConfigurationPropertiesScan({"com.company.common.cache.internal.configuration"})
@EnableAutoConfiguration(
        exclude = {FreeMarkerAutoConfiguration.class}
)
public class CaffeineCacheManagerConfig {

    Caffeine<Object, Object> caffeine;

    @Autowired
    public CaffeineCacheManagerConfig(Caffeine<Object, Object> caffeine) {
        this.caffeine = caffeine;
    }

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(this.caffeine);
        return cacheManager;
    }
}
