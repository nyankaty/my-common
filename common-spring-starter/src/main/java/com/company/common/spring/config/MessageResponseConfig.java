package com.company.common.spring.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "message-response"
)
public class MessageResponseConfig {
    private Map<String, String> params;

    public MessageResponseConfig() {
        // no arg constructor
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(final Map<String, String> params) {
        this.params = params;
    }

    public String toString() {
        return "MessageResponseConfig(params=" + this.getParams() + ")";
    }
}

