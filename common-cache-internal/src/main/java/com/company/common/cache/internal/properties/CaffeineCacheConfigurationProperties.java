package com.company.common.cache.internal.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = {"app.cache.internal.enable"},
        havingValue = "true"
)
@ConfigurationProperties(
        prefix = "app.cache.internal"
)
public class CaffeineCacheConfigurationProperties {

    @Value("${app.cache.internal.applicationShortName}")
    String applicationShortName;

    @Value("${app.cache.internal.expireAfterWrite}")
    Long expireAfterWrite;

    @Value("${app.cache.internal.expireAfterAccess}")
    Long expireAfterAccess;

    @Value("${app.cache.internal.refreshAfterWrite}")
    Long refreshAfterWrite;

    @Value("${app.cache.internal.enableRecordStats}")
    Boolean enableRecordStats;

    @Value("${app.cache.internal.cacheTemporaryDisable}")
    Boolean cacheTemporaryDisable;

    @Value("${app.cache.internal.removalListener}")
    Boolean removalListener;

    public String getApplicationShortName() {
        return this.applicationShortName;
    }

    public Long getExpireAfterWrite() {
        return this.expireAfterWrite;
    }

    public Long getExpireAfterAccess() {
        return this.expireAfterAccess;
    }

    public Long getRefreshAfterWrite() {
        return this.refreshAfterWrite;
    }

    public boolean getEnableRecordStats() {
        return this.enableRecordStats;
    }

    public boolean getCacheTemporaryDisable() {
        return this.cacheTemporaryDisable;
    }

    public boolean getRemovalListener() {
        return this.removalListener;
    }

    public void setApplicationShortName(String applicationShortName) {
        this.applicationShortName = applicationShortName;
    }

    public void setExpireAfterWrite(long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public void setExpireAfterAccess(long expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
    }

    public void setRefreshAfterWrite(long refreshAfterWrite) {
        this.refreshAfterWrite = refreshAfterWrite;
    }

    public void setEnableRecordStats(boolean enableRecordStats) {
        this.enableRecordStats = enableRecordStats;
    }

    public void setCacheTemporaryDisable(boolean cacheTemporaryDisable) {
        this.cacheTemporaryDisable = cacheTemporaryDisable;
    }

    public void setRemovalListener(boolean removalListener) {
        this.removalListener = removalListener;
    }
}
