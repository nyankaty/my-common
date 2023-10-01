package com.company.common.util.enums;

public interface ResponseStatus {

    int getHttpCode();

    String getCode();

    String getName();

    String getMessage();
}
