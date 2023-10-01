package com.company.common.spring.factory.response;

import com.company.common.util.enums.ResponseStatus;

public class ErrorTrace<T> extends Response<T> {

    private Object errorDetail;
    private StackTraceElement[] stackTrace;

    public ErrorTrace(Object errorDetail, ResponseStatus status) {
        super(status);
        this.errorDetail = errorDetail;
    }

    public ErrorTrace(ResponseStatus status) {
        super(status);
    }

    public ErrorTrace(Throwable throwable, ResponseStatus status) {
        super(status);
        this.stackTrace = throwable.getStackTrace();
        this.errorDetail = throwable.getMessage();
    }

    public Object getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(Object errorDetail) {
        this.errorDetail = errorDetail;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }
}
