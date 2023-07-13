package com.company.common.cache.internal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "app.cache.internal"
)
public class CaffeineCacheConfigurationProperties {
    String applicationShortName = "demoCacheApp";
    Long expireAfterWrite = null;
    Long expireAfterAccess = null;
    Long refreshAfterWrite = null;
    Boolean enableRecordStats = true;
    Boolean cacheTemporaryDisable = false;
    Boolean removalListener = true;

    public CaffeineCacheConfigurationProperties() {
        // no arg constructor
    }

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
