package com.company.common.cache.internal.implementation;

import java.lang.reflect.Method;

import com.company.common.cache.internal.properties.CaffeineCacheConfigurationProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

public class FullKeyGenerator implements KeyGenerator {

    CaffeineCacheConfigurationProperties properties;

    public FullKeyGenerator(CaffeineCacheConfigurationProperties properties) {
        this.properties = properties;
    }

    public Object generate(Object target, Method method, Object... params) {
        return this.properties.getApplicationShortName() + "::" + target.getClass().getName() + "_" + method.getName() +  + StringUtils.arrayToDelimitedString(params, "_");
    }
}
