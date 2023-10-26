package com.company.common.redis.properties;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@ConditionalOnProperty(
        value = {"spring.redis.enabled"},
        havingValue = "true"
)
@Configuration
@Primary
@ConfigurationProperties(
        prefix = "spring.cache"
)
public class CacheConfigurationProperties extends CacheProperties {

    private Map<String, CacheProperties.Redis> customCache;

    private String delimiter;

    public Map<String, CacheProperties.Redis> getCustomCache() {
        return customCache;
    }

    public void setCustomCache(Map<String, CacheProperties.Redis> customCache) {
        this.customCache = customCache;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
