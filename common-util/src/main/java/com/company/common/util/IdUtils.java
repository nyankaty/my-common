package com.company.common.util;

import java.util.UUID;

public final class IdUtils {

    private IdUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String nextId() {
        // need fix for add partition info
        return UUID.randomUUID().toString();
    }
}
