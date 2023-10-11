package com.company.common.cache.external.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.cache.external.custom-expirations")
public class CustomCacheExpirationsProperties {

    private Map<String, Long> cacheExpirations = new HashMap<>();

    public Map<String, Long> getCacheExpirations() {
        return cacheExpirations;
    }
}
