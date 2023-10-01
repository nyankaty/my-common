package com.company.common.spring.factory.response;

public class FieldErrorResponse {

    private String objectName;
    private String fieldName;
    private String message;

    public FieldErrorResponse(String objectName, String fieldName, String message) {
        this.objectName = objectName;
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
