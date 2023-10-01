package com.company.common.spring.exception;

import com.company.common.util.enums.ResponseStatus;

public class ResponseException extends RuntimeException {
    private static final long serialVersionUID = 4487631991290161890L;

    private final ResponseStatus status;
    private Object errorDetail;

    public ResponseException(ResponseStatus status) {
        this.status = status;
    }

    public ResponseException(Object errorDetail, ResponseStatus status) {
        this.status = status;
        this.errorDetail = errorDetail;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Object getErrorDetail() {
        return errorDetail;
    }
}
