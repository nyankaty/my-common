package com.company.common.redis.port;

import java.time.Duration;
import java.util.Set;

public interface CacheTemplate {

    Object getObject(String key);

    Object getObject(String cacheName, String key);

    void putObject(String cacheName, String key, Object value);

    void putObject(String key, Object value);

    void putObject(String cacheName, String key, Object value, Duration duration);

    void putObject(String key, Object value, Duration duration);

    boolean hasKey(String cacheName, String key);

    boolean hasKey(String key);

    void invalidate(String cacheName, String key);

    void invalidate(String key);

    Set<String> keySet(String keyPattern, long count);
}
