package com.company.common.spring.exception;

import com.company.common.spring.constant.ResponseStatusCode;
import com.company.common.spring.factory.response.Response;

import java.util.Map;

@SuppressWarnings("java:S1165")
public class BaseResponseException extends RuntimeException {
    private final ResponseStatusCode responseStatusCode;
    private Response dataResponse;
    private Map<String, String> params;

    public BaseResponseException(ResponseStatusCode responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public BaseResponseException(Response dataResponse, ResponseStatusCode responseStatusCode) {
        this.dataResponse = dataResponse;
        this.responseStatusCode = responseStatusCode;
    }

    public BaseResponseException(Response dataResponse, ResponseStatusCode responseStatusCode, Map<String, String> params) {
        this.dataResponse = dataResponse;
        this.responseStatusCode = responseStatusCode;
        this.params = params;
    }

    public ResponseStatusCode getResponseStatusCode() {
        return this.responseStatusCode;
    }

    public Response getDataResponse() {
        return this.dataResponse;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setDataResponse(final Response dataResponse) {
        this.dataResponse = dataResponse;
    }

    public void setParams(final Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "BaseResponseException(responseStatusCode=" + this.getResponseStatusCode() + ", dataResponse=" + this.getDataResponse() + ", params=" + this.getParams() + ")";
    }
}
