package com.company.common.redis.implementation;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FullKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getName() + "." + method.getName() + "(" + StringUtils.arrayToDelimitedString(params, "_") + ")";
    }
}
