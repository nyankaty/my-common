package com.company.common.cache.internal.port;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.util.Set;

public interface InternalCacheTemplate {
    Object get(String var1);

    void put(String var1, Object var2);

    void invalidate(String var1);

    void clear();

    Set<Object> keySet();

    CacheStats stats();
}
