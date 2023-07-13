package com.company.common.cache.external.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(
        prefix = "app.cache.external"
)
public class RedisCacheConfigurationProperties {
    private String applicationShortName = "demoCacheApp";
    private String delimiter;
    private int port = 6379;
    private String host = "localhost";
    private List<String> nodes = new ArrayList<>();
    private Long cacheDefaultExpiration = null;
    private Map<String, Long> cacheExpirations = new HashMap<>();
    private String password = "";

    public RedisCacheConfigurationProperties() {
        // no arg constructor
    }

    public String getApplicationShortName() {
        return this.applicationShortName;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public int getPort() {
        return this.port;
    }

    public String getHost() {
        return this.host;
    }

    public List<String> getNodes() {
        return this.nodes;
    }

    public Long getCacheDefaultExpiration() {
        return this.cacheDefaultExpiration;
    }

    public Map<String, Long> getCacheExpirations() {
        return this.cacheExpirations;
    }

    public String getPassword() {
        return this.password;
    }

    public void setApplicationShortName(String applicationShortName) {
        this.applicationShortName = applicationShortName;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public void setCacheDefaultExpiration(Long cacheDefaultExpiration) {
        this.cacheDefaultExpiration = cacheDefaultExpiration;
    }

    public void setCacheExpirations(Map<String, Long> cacheExpirations) {
        this.cacheExpirations = cacheExpirations;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "RedisCacheConfigurationProperties(port=" + this.getPort() + ", host=" + this.getHost() + ", nodes=" + this.getNodes() + ", cacheExpirations=" + this.getCacheExpirations() + ", password=" + this.getPassword() + ")";
    }
}
