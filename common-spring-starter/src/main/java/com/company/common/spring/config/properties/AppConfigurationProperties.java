package com.company.common.spring.config.properties;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "app"
)
@RefreshScope
public class AppConfigurationProperties {

    @Value("${app.application-short-name}")
    private String applicationShortName;

    @Value("${app.application-context-name}")
    private String applicationContextName;

    @Value("${app.log-request-http}")
    private boolean logRequestHttp;

    @Value("${app.default-service-enable-log-request}")
    private boolean defaultServiceEnableLogRequest;

    @Value("${app.repository-query-limit-warning-ms}")
    private int repositoryQueryLimitWarningMs;

    @Value("${app.async-executor-core-pool-size}")
    private int asyncExecutorCorePoolSize;

    @Value("${app.async-executor-max-pool-size}")
    private int asyncExecutorMaxPoolSize;

    @Value("${app.async-executor-keep-alive-seconds}")
    private int keepAliveSeconds;

    @Value("${app.async-executor-queue-capacity}")
    private int queueCapacity;

    @Value("${app.time-trace-enabled}")
    private boolean timeTraceEnabled;

    private String asyncExecutorThreadNamePrefix = "Async-";
    private List<String> localeResolverLanguages = Arrays.asList("en", "vi");
    private String defaultLanguage = "en";

    public String getApplicationShortName() {
        return this.applicationShortName;
    }

    public String getApplicationContextName() {
        return this.applicationContextName;
    }

    public boolean isLogRequestHttp() {
        return this.logRequestHttp;
    }

    public boolean isDefaultServiceEnableLogRequest() {
        return this.defaultServiceEnableLogRequest;
    }

    public int getRepositoryQueryLimitWarningMs() {
        return this.repositoryQueryLimitWarningMs;
    }

    public int getAsyncExecutorCorePoolSize() {
        return this.asyncExecutorCorePoolSize;
    }

    public int getAsyncExecutorMaxPoolSize() {
        return this.asyncExecutorMaxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public List<String> getLocaleResolverLanguages() {
        return this.localeResolverLanguages;
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public boolean isTimeTraceEnabled() {
        return this.timeTraceEnabled;
    }

    public String getAsyncExecutorThreadNamePrefix() {
        return this.asyncExecutorThreadNamePrefix;
    }

    public void setApplicationShortName(final String applicationShortName) {
        this.applicationShortName = applicationShortName;
    }

    public void setApplicationContextName(final String applicationContextName) {
        this.applicationContextName = applicationContextName;
    }

    public void setLogRequestHttp(final boolean logRequestHttp) {
        this.logRequestHttp = logRequestHttp;
    }

    public void setDefaultServiceEnableLogRequest(final boolean defaultServiceEnableLogRequest) {
        this.defaultServiceEnableLogRequest = defaultServiceEnableLogRequest;
    }

    public void setRepositoryQueryLimitWarningMs(final int repositoryQueryLimitWarningMs) {
        this.repositoryQueryLimitWarningMs = repositoryQueryLimitWarningMs;
    }

    public void setAsyncExecutorCorePoolSize(final int asyncExecutorCorePoolSize) {
        this.asyncExecutorCorePoolSize = asyncExecutorCorePoolSize;
    }

    public void setAsyncExecutorMaxPoolSize(final int asyncExecutorMaxPoolSize) {
        this.asyncExecutorMaxPoolSize = asyncExecutorMaxPoolSize;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public void setLocaleResolverLanguages(final List<String> localeResolverLanguages) {
        this.localeResolverLanguages = localeResolverLanguages;
    }

    public void setDefaultLanguage(final String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void setTimeTraceEnabled(final boolean timeTraceEnabled) {
        this.timeTraceEnabled = timeTraceEnabled;
    }

    public void setAsyncExecutorThreadNamePrefix(final String asyncExecutorThreadNamePrefix) {
        this.asyncExecutorThreadNamePrefix = asyncExecutorThreadNamePrefix;
    }

    public String toString() {
        return "AppConfigurationProperties(applicationShortName=" + this.getApplicationShortName() + ", applicationContextName=" + this.getApplicationContextName() + ", logRequestHttp=" + this.isLogRequestHttp() + ", defaultServiceEnableLogRequest=" + this.isDefaultServiceEnableLogRequest() + ", repositoryQueryLimitWarningMs=" + this.getRepositoryQueryLimitWarningMs() + ", asyncExecutorCorePoolSize=" + this.getAsyncExecutorCorePoolSize() + ", asyncExecutorMaxPoolSize=" + this.getAsyncExecutorMaxPoolSize() + ", localeResolverLanguages=" + this.getLocaleResolverLanguages() + ", defaultLanguage=" + this.getDefaultLanguage() + ", timeTraceEnabled=" + this.isTimeTraceEnabled() + ", asyncExecutorThreadNamePrefix=" + this.getAsyncExecutorThreadNamePrefix() + ")";
    }
}
