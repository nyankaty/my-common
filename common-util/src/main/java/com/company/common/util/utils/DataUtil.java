package com.company.common.util.utils;

import com.company.common.util.constant.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataUtil {

    private static final Logger log = LoggerFactory.getLogger(DataUtil.class);

    private static final String SEARCH_REGEX = "(.*?)";

    private DataUtil() {}

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static <T> String parseDataToContent(
            String content, T templateData, String characterBefore, String characterAfter) {
        List<String> params = new ArrayList<>();
        String regexSearch =
                StringPool.BACK_SLASH
                        + characterBefore
                        + SEARCH_REGEX
                        + StringPool.BACK_SLASH
                        + characterAfter;

        Pattern regex = Pattern.compile(regexSearch);

        Matcher regexMatcher = regex.matcher(content);

        while (regexMatcher.find()) {
            params.add(regexMatcher.group(1));
        }

        Class<?> clazz = templateData.getClass();

        List<Field> allFields = FieldUtils.getAllFieldsList(clazz);

        Map<String, Object> values = new HashMap<>();

        for (Field field : allFields) {
            field.setAccessible(true);

            String fieldName = field.getName();

            if (params.contains(fieldName)) {
                try {
                    Object valueOfAttribute = field.get(templateData);
                    values.put(characterBefore + fieldName + characterAfter, valueOfAttribute);
                } catch (IllegalAccessException e) {
                    log.error(
                            "Error when get value by filed {} of object {}",
                            fieldName,
                            templateData);
                }
            }
        }

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            content = content.replace(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return content;
    }
}
