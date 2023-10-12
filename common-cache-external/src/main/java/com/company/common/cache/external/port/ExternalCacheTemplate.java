package com.company.common.cache.external.port;

import java.util.List;
import java.util.Set;

public interface ExternalCacheTemplate {

    <T> T getObject(String key);

    <T> List<T> getObjectAsList(String cacheName, String key, Class<T> objectClass);

    <T> T getObjectFromList(String cacheName, String key, Class<T> objectClass);

    <T> T getObjectFromList(String key, Class<T> objectClass);

    <T> List<T> getObjectAsList(String key, Class<T> objectClass);

    <T> String putObject(String cacheName, String key, T value);

    <T> String putObject(String key, T value);

    <T> String putObjectAsList(String cacheName, String key, List<T> valueList);

    <T> String putObjectAsList(String key, List<T> value);

    <T> String putObjectToList(String cacheName, String key, T value);

    <T> String putObjectToList(String key, T value);

    boolean hasKey(String cacheName, String key);

    boolean hasKey(String key);

    void invalidate(String cacheName, String key);

    void invalidate(String key);

    Set<String> keySet(String keyPattern, long count);
}
