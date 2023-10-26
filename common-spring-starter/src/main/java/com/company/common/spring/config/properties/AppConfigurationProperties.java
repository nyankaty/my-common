package com.company.common.spring.config.properties;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class AppConfigurationProperties {

    @Value("${application-short-name}")
    private String applicationShortName;

    @Value("${application-context-name}")
    private String applicationContextName;

    @Value("${app.log.log-request-http}")
    private boolean logRequestHttp;

    @Value("${app.log.default-service-enable-log-request}")
    private boolean defaultServiceEnableLogRequest;

    @Value("${app.log.repository-query-limit-warning-ms}")
    private int repositoryQueryLimitWarningMs;

    @Value("${app.log.time-trace-enabled}")
    private boolean timeTraceEnabled;

    private List<String> localeResolverLanguages = Arrays.asList("en", "vi");
    private String defaultLanguage = "en";

    public String getApplicationShortName() {
        return applicationShortName;
    }

    public void setApplicationShortName(String applicationShortName) {
        this.applicationShortName = applicationShortName;
    }

    public String getApplicationContextName() {
        return applicationContextName;
    }

    public void setApplicationContextName(String applicationContextName) {
        this.applicationContextName = applicationContextName;
    }

    public boolean isLogRequestHttp() {
        return logRequestHttp;
    }

    public void setLogRequestHttp(boolean logRequestHttp) {
        this.logRequestHttp = logRequestHttp;
    }

    public boolean isDefaultServiceEnableLogRequest() {
        return defaultServiceEnableLogRequest;
    }

    public void setDefaultServiceEnableLogRequest(boolean defaultServiceEnableLogRequest) {
        this.defaultServiceEnableLogRequest = defaultServiceEnableLogRequest;
    }

    public int getRepositoryQueryLimitWarningMs() {
        return repositoryQueryLimitWarningMs;
    }

    public void setRepositoryQueryLimitWarningMs(int repositoryQueryLimitWarningMs) {
        this.repositoryQueryLimitWarningMs = repositoryQueryLimitWarningMs;
    }

    public boolean isTimeTraceEnabled() {
        return timeTraceEnabled;
    }

    public void setTimeTraceEnabled(boolean timeTraceEnabled) {
        this.timeTraceEnabled = timeTraceEnabled;
    }

    public List<String> getLocaleResolverLanguages() {
        return localeResolverLanguages;
    }

    public void setLocaleResolverLanguages(List<String> localeResolverLanguages) {
        this.localeResolverLanguages = localeResolverLanguages;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
