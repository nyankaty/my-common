package com.company.common.spring.factory.response;

import com.company.common.util.enums.ResponseStatus;
import com.company.common.util.enums.ResponseStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class Response<T> {

    private T data;
    private boolean success;
    private String status;
    private String code;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date responseTime;

    public Response(ResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.status = status.getName();
        this.responseTime = new Date();
    }

    public Response(T data, ResponseStatus status) {
        this.data = data;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.status = status.getName();
        this.responseTime = new Date();
    }

    public static <T> Response<T> of(T data) {
        return success(data, ResponseStatusEnum.SUCCESS);
    }

    public static <T> Response<T> ok() {
        return success(null, ResponseStatusEnum.SUCCESS);
    }

    public static <T> Response<T> success(T data, ResponseStatus status) {
        Response<T> response = new Response<>(data, status);
        response.setSuccess(true);

        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }
}
