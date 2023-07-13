package com.company.common.cache.internal.implementation;

import java.lang.reflect.Method;

import com.company.common.cache.internal.properties.CaffeineCacheConfigurationProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

public class FullKeyGenerator implements KeyGenerator {

    CaffeineCacheConfigurationProperties properties;

    public FullKeyGenerator(CaffeineCacheConfigurationProperties properties) {
        this.properties = properties;
    }

    @NotNull
    public Object generate(Object target, Method method, @NotNull Object... params) {
        return this.properties.getApplicationShortName() + "::" + target.getClass().getSimpleName() + "_" + method.getName() + "_" + StringUtils.arrayToDelimitedString(params, "_");
    }
}
