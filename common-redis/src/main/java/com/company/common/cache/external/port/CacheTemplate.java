package com.company.common.cache.external.port;

import java.time.Duration;

public interface CacheTemplate {

    <T> T getObject(String key);

    <T> T getObject(String cacheName, String key);

    void putObject(String cacheName, String key, Object value);

    void putObject(String key, Object value);

    void putObject(String cacheName, String key, Object value, Duration duration);

    void putObject(String key, Object value, Duration duration);

    boolean hasKey(String cacheName, String key);

    boolean hasKey(String key);

    void invalidate(String cacheName, String key);

    void invalidate(String key);
}
