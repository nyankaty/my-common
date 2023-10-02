package com.company.common.spring.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "info.build"
)
@RefreshScope
public class InfoConfigurationProperties {

    @Value("${info.build.artifact}")
    private String artifact;

    @Value("${info.build.name}")
    private String name;

    @Value("${info.build.description}")
    private String description;

    @Value("${info.build.version}")
    private String version;

    public String getArtifact() {
        return this.artifact;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVersion() {
        return this.version;
    }

    public void setArtifact(final String artifact) {
        this.artifact = artifact;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String toString() {
        return "InfoConfigurationProperties(artifact=" + this.getArtifact() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", version=" + this.getVersion() + ")";
    }
}

