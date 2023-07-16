package com.company.common.spring.factory.response;

import com.company.common.spring.locale.Translator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.Serializable;
import java.util.Date;

public class Status implements Serializable {

    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("responseTime")
    @JsonFormat(
            shape = Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date responseTime;

    @JsonProperty("displayMessage")
    private String displayMessage;

    public Status() {
    }

    public Status(final String code, final String message, final Date responseTime, final String displayMessage) {
        this.code = code;
        this.message = message;
        this.responseTime = responseTime;
        this.displayMessage = displayMessage;
    }

    public Status(String code, boolean setMessageImplicitly) {
        this.setCode(code, setMessageImplicitly);
    }

    public void setCode(String code, boolean setMessageImplicitly) {
        this.code = code;
        if (setMessageImplicitly) {
            this.message = Translator.toLocale(code);
        }

        this.displayMessage = this.message;
    }

    public void setCode(String code) {
        this.setCode(code, true);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getResponseTime() {
        return this.responseTime;
    }

    public String getDisplayMessage() {
        return this.displayMessage;
    }

    @JsonProperty("message")
    public void setMessage(final String message) {
        this.message = message;
    }

    @JsonProperty("responseTime")
    public void setResponseTime(final Date responseTime) {
        this.responseTime = responseTime;
    }

    @JsonProperty("displayMessage")
    public void setDisplayMessage(final String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String toString() {
        return "ResponseStatus(code=" + this.getCode() + ", message=" + this.getMessage() + ", responseTime=" + this.getResponseTime() + ", displayMessage=" + this.getDisplayMessage() + ")";
    }
}

