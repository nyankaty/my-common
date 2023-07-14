package com.company.common.spring.utils;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("java:S1068")
public final class IdUtils {

    private static final char[] ENCODING_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int MASK = 31;
    private static final int MASK_BITS = 5;
    private static final long TIMESTAMP_OVERFLOW_MASK = -281474976710656L;
    private static final Random random = new Random();

    public static String nextUUID() {
        // need fix for add partition info
        return UUID.randomUUID().toString();
    }

    public static String nextULID() {
        return nextULID(System.currentTimeMillis());
    }

    private static String nextULID(long timestamp) {
        return internalUIDString(timestamp);
    }

    private static void internalWriteCrockford(char[] buffer, long value, int count, int offset) {
        for(int i = 0; i < count; ++i) {
            int index = (int)(value >>> (count - i - 1) * 5 & 31L);
            buffer[offset + i] = ENCODING_CHARS[index];
        }

    }

    private static String internalUIDString(long timestamp) {
        checkTimestamp(timestamp);
        char[] buffer = new char[26];
        internalWriteCrockford(buffer, timestamp, 10, 0);
        internalWriteCrockford(buffer, random.nextLong(), 8, 10);
        internalWriteCrockford(buffer, random.nextLong(), 8, 18);
        return new String(buffer);
    }

    private static void checkTimestamp(long timestamp) {
        if ((timestamp & -281474976710656L) != 0L) {
            throw new IllegalArgumentException("ULID does not support timestamps after +10889-08-02T05:31:50.655Z!");
        }
    }

    private IdUtils() {
        throw new IllegalStateException("Utility class");
    }
}
