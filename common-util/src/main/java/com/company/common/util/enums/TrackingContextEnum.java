package com.company.common.util.enums;

public enum TrackingContextEnum {
    X_FORWARD_FOR("X-Forwarded-For", "forwardIP"),
    X_REAL_IP("X-Real-Ip", "clientIP"),
    X_REQUEST_ID("X-Request-Id", "requestID"),
    X_CORRELATION_ID("X-Correlation-Id", "correlationID");

    private final String headerKey;
    private final String threadKey;

    public String getHeaderKey() {
        return this.headerKey;
    }

    public String getThreadKey() {
        return this.threadKey;
    }

    TrackingContextEnum(final String headerKey, final String threadKey) {
        this.headerKey = headerKey;
        this.threadKey = threadKey;
    }
}
