package com.company.common.cache.external.port;

import java.util.List;
import java.util.Set;

public interface ExternalCacheTemplate {
    <T> T getObject(String var1, String var2, Class<T> var3);

    <T> T getObject(String var1, Class<T> var2);

    <T> List<T> getObjectAsList(String var1, String var2, Class<T> var3);

    <T> T getObjectFromList(String var1, String var2, Class<T> var3);

    <T> T getObjectFromList(String var1, Class<T> var2);

    <T> List<T> getObjectAsList(String var1, Class<T> var2);

    <T> String putObject(String var1, String var2, T var3);

    <T> String putObject(String var1, T var2);

    <T> String putObjectAsList(String var1, String var2, List<T> var3);

    <T> String putObjectAsList(String var1, List<T> var2);

    <T> String putObjectToList(String var1, String var2, T var3);

    <T> String putObjectToList(String var1, T var2);

    boolean hasKey(String var1, String var2);

    boolean hasKey(String var1);

    void invalidate(String var1, String var2);

    void invalidate(String var1);

    Set<String> keySet(String var1, long var2);
}
