package com.company.common.util.enums;

public enum ResponseStatusEnum implements ResponseStatus {
    SUCCESS(200, "200", "OK"),
    CREATED(201, "201", "Created"),
    ACCEPTED(202, "202", "Accepted"),
    NO_CONTENT(204, "204", "No Content"),
    BAD_REQUEST(400, "400", "Bad Request"),
    UNAUTHORIZED(401, "401", "Unauthorized"),
    FORBIDDEN(403, "403", "Forbidden"),
    NOT_FOUND(404, "404", "Not Found"),
    METHOD_NOT_ALLOWED(405, "405", "Method Not Allowed"),
    REQUEST_TIMEOUT(408, "408", "Request Timeout"),
    PAYLOAD_TOO_LARGE(413, "413", "Payload Too Large"),
    UNSUPPORTED_MEDIA_TYPE(415, "415", "Unsupported Media Type"),
    LOCKED(423, "423", "Locked"),
    TOO_MANY_REQUESTS(429, "429", "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "431", "Request Header Fields Too Large"),
    INTERNAL_SERVER_ERROR(500, "500", "Internal Server Error"),
    NOT_IMPLEMENTED(501, "501", "Not Implemented"),
    BAD_GATEWAY(502, "502", "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "503", "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "504", "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "505", "HTTP Version not supported"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "509", "Bandwidth Limit Exceeded"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "511", "Network Authentication Required");

    private final int httpCode;
    private final String code;
    private final String message;

    ResponseStatusEnum(int httpCode, String code, String message) {
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
