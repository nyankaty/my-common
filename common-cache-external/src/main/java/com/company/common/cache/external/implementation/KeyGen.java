package com.company.common.cache.external.implementation;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

public class KeyGen implements KeyGenerator {

    /**
     * Returns a key combine by path reference class name concat with method name and concat all method params.
     * <br><br>
     * For example <br>
     * return: <b>com.program.Calculator.twoSum(1_3)</b><br>
     * return: <b>com.program.Calculator.init()</b> if there are no method params
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getName() + "." + method.getName() + "(" + StringUtils.arrayToDelimitedString(params, "_") + ")";
    }

    /**
     * Returns a key combine by path reference class name concat with method name and concat key param.
     * <br><br>
     * For example <br>
     * return: <b>com.program.User.get(1)</b>
     */
    public static String generate(Object target, Method method, Object key) {
        return target.getClass().getName() + "." + method.getName() + "(" + key + ")";
    }
}
