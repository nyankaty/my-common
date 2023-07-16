package com.company.common.spring.constant;

public enum TrackingContextEnum {
    X_FORWARD_FOR("x-forwarded-for", "forwardIP"),
    X_REAL_IP("x-real-ip", "clientIP"),
    X_REQUEST_ID("x-request-id", "requestID"),
    X_CORRELATION_ID("X-Correlation-ID", "correlationID");

    private final String headerKey;
    private final String threadKey;

    public String getHeaderKey() {
        return this.headerKey;
    }

    public String getThreadKey() {
        return this.threadKey;
    }

    private TrackingContextEnum(final String headerKey, final String threadKey) {
        this.headerKey = headerKey;
        this.threadKey = threadKey;
    }
}
