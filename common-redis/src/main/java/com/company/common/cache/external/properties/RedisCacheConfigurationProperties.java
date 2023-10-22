package com.company.common.cache.external.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(
        value = {"app.cache.external.enable"},
        havingValue = "true"
)
@ConfigurationProperties(
        prefix = "app.cache.external"
)
public class RedisCacheConfigurationProperties {

    @Value("${app.cache.external.applicationShortName}")
    private String applicationShortName;

    @Value("${app.cache.external.delimiter}")
    private String delimiter;

    @Value("${app.cache.external.cacheDefaultExpiration}")
    private Long cacheDefaultExpiration;

    @Value("${app.cache.external.scanLimit}")
    private Long scanLimit;

    private Map<String, Long> cacheExpirations = new HashMap<>();

    @Value("${app.cache.external.host}")
    private String host;

    @Value("${app.cache.external.port}")
    private int port;

    @Value("${app.cache.external.username}")
    private String username;

    @Value("${app.cache.external.password}")
    private String password;

    @Value("${app.cache.external.nodes}")
    private List<String> nodes = new ArrayList<>();

    @Value("${app.cache.external.cluster.enable}")
    private boolean isCluster;

    @Value("${app.cache.external.sentinel.enable}")
    private boolean isSentinel;

    @Value("${app.cache.external.sentinel.master}")
    private String sentinelMaster;

    public String getApplicationShortName() {
        return applicationShortName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public Long getCacheDefaultExpiration() {
        return cacheDefaultExpiration;
    }

    public Long getScanLimit() {
        return scanLimit;
    }

    public Map<String, Long> getCacheExpirations() {
        return cacheExpirations;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public boolean isCluster() {
        return isCluster;
    }

    public boolean isSentinel() {
        return isSentinel;
    }

    public String getSentinelMaster() {
        return sentinelMaster;
    }
}
