package com.company.common.cache.implementation;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import org.jetbrains.annotations.NotNull;

public class FullKeyGenerator implements KeyGenerator {

    public FullKeyGenerator() {
        // no arg constructor
    }

    @NotNull
    public Object generate(Object target, Method method, Object @NotNull ... params) {
        return target.getClass().getSimpleName() + "_" + method.getName() + "_" + StringUtils.arrayToDelimitedString(params, "_");
    }
}
