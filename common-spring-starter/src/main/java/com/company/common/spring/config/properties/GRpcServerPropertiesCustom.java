package com.company.common.spring.config.properties;

import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties(
        prefix = "grpc")
@ConditionalOnProperty(
        value = {"grpc.enabled"},
        havingValue = "true"
)
@RefreshScope
@Primary
public class GRpcServerPropertiesCustom extends GRpcServerProperties {

    @Value("${grpc.client-request-timeout-ms}")
    private Long clientRequestTimeoutMs;

    @Value("${grpc.client-request-log}")
    private boolean clientRequestLog;

    @Value("${grpc.server-log}")
    private boolean serverLog;

    public GRpcServerPropertiesCustom() {
        // no arg constructor
    }

    public Long getClientRequestTimeoutMs() {
        return this.clientRequestTimeoutMs;
    }

    public boolean isClientRequestLog() {
        return this.clientRequestLog;
    }

    public boolean isServerLog() {
        return this.serverLog;
    }

    public void setClientRequestTimeoutMs(final Long clientRequestTimeoutMs) {
        this.clientRequestTimeoutMs = clientRequestTimeoutMs;
    }

    public void setClientRequestLog(final boolean clientRequestLog) {
        this.clientRequestLog = clientRequestLog;
    }

    public void setServerLog(final boolean serverLog) {
        this.serverLog = serverLog;
    }

    public String toString() {
        return "GRpcServerPropertiesCustom(clientRequestTimeoutMs=" + this.getClientRequestTimeoutMs() + ", clientRequestLog=" + this.isClientRequestLog() + ", serverLog=" + this.isServerLog() + ")";
    }
}

