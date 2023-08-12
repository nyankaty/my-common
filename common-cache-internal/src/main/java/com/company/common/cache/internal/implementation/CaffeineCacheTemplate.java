package com.company.common.cache.internal.implementation;

import com.company.common.cache.internal.port.InternalCacheTemplate;
import com.company.common.cache.internal.properties.CaffeineCacheConfigurationProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        value = {"app.cache.internal.enable"},
        havingValue = "true"
)
@Component
@ComponentScan(
        basePackages = {"com.company.common.cache.internal.properties", "com.company.common.cache.internal.configuration"}
)
public class CaffeineCacheTemplate implements InternalCacheTemplate {

    @Autowired
    Cache<Object, Object> caffeineCache;

    @Autowired
    CaffeineCacheConfigurationProperties properties;

    public CaffeineCacheTemplate() {
        // no arg constructor
    }

    private String keyGen(Object key) {
        return this.properties.getApplicationShortName() + "::" + key;
    }

    public Object get(String key) {
        String keyGen = this.keyGen(key);
        return this.caffeineCache.getIfPresent(keyGen);
    }

    public void put(String key, Object value) {
        String keyGen = this.keyGen(key);
        this.caffeineCache.put(keyGen, value);
    }

    public void invalidate(String key) {
        String keyGen = this.keyGen(key);
        this.caffeineCache.invalidate(keyGen);
    }

    public void clear() {
        this.caffeineCache.invalidateAll();
    }

    public Set<Object> keySet() {
        ConcurrentMap<Object, Object> map = this.caffeineCache.asMap();
        return map.keySet();
    }

    public CacheStats stats() {
        return this.caffeineCache.stats();
    }
}
