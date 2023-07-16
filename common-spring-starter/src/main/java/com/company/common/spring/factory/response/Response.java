package com.company.common.spring.factory.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("data")
    private T data;

    public Response(T data) {
        this.data = data;
    }

    public String toString() {
        return "{status=" + this.status + ", data=" + this.data.toString() + '}';
    }

    public Status getStatus() {
        return this.status;
    }

    public T getData() {
        return this.data;
    }

    @JsonProperty("status")
    public void setStatus(final Status status) {
        this.status = status;
    }

    @JsonProperty("data")
    public void setData(final T data) {
        this.data = data;
    }

    public Response() {
    }
}
