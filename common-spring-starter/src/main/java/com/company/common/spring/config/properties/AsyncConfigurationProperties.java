package com.company.common.spring.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(
        prefix = "app.executor"
)
@Configuration
@RefreshScope
public class AsyncConfigurationProperties {

    @Value("${app.executor.async-executor-core-pool-size}")
    private int asyncExecutorCorePoolSize;

    @Value("${app.executor.async-executor-max-pool-size}")
    private int asyncExecutorMaxPoolSize;

    @Value("${app.executor.async-executor-keep-alive-seconds}")
    private int keepAliveSeconds;

    @Value("${app.executor.async-executor-queue-capacity}")
    private int queueCapacity;

    private String asyncExecutorThreadNamePrefix = "Async-";

    public int getAsyncExecutorCorePoolSize() {
        return asyncExecutorCorePoolSize;
    }

    public void setAsyncExecutorCorePoolSize(int asyncExecutorCorePoolSize) {
        this.asyncExecutorCorePoolSize = asyncExecutorCorePoolSize;
    }

    public int getAsyncExecutorMaxPoolSize() {
        return asyncExecutorMaxPoolSize;
    }

    public void setAsyncExecutorMaxPoolSize(int asyncExecutorMaxPoolSize) {
        this.asyncExecutorMaxPoolSize = asyncExecutorMaxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getAsyncExecutorThreadNamePrefix() {
        return asyncExecutorThreadNamePrefix;
    }

    public void setAsyncExecutorThreadNamePrefix(String asyncExecutorThreadNamePrefix) {
        this.asyncExecutorThreadNamePrefix = asyncExecutorThreadNamePrefix;
    }
}
