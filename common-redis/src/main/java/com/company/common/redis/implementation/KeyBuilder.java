package com.company.common.redis.implementation;

import java.lang.reflect.Method;

public class KeyBuilder {

    public static String hash(Object target, Method method, Object key) {
        return target.getClass().getName() + "." + method.getName() + "(" + key + ")";
    }

    public static String hash(Object target, Method method) {
        return target.getClass().getName() + "." + method.getName() + "()";
    }

    private KeyBuilder() {
        throw new IllegalStateException("Utility class");
    }
}
