package com.company.common.spring.constant;

public final class ResponseStatusCodeEnum {
    public static final ResponseStatusCode SUCCESS = ResponseStatusCode.builder().code("00").httpCode(200).build();
    public static final ResponseStatusCode BUSINESS_ERROR = ResponseStatusCode.builder().code("BSA0001").httpCode(400).build();
    public static final ResponseStatusCode VALIDATION_ERROR = ResponseStatusCode.builder().code("BSA0002").httpCode(400).build();
    public static final ResponseStatusCode INTERNAL_GENERAL_SERVER_ERROR = ResponseStatusCode.builder().code("BSA0003").httpCode(500).build();
    public static final ResponseStatusCode ERROR_BODY_CLIENT = ResponseStatusCode.builder().code("BSA0004").httpCode(400).build();
    public static final ResponseStatusCode ERROR_BODY_REQUIRED = ResponseStatusCode.builder().code("BSA0005").httpCode(400).build();

    private ResponseStatusCodeEnum() {
        throw new IllegalStateException("Utility class");
    }
}
