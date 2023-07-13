package com.company.common.spring.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConfigurationProperties(
        prefix = "springdoc.api-docs"
)
@Configuration
@Primary
@RefreshScope
public class SwaggerConfigurationProperties {
    private boolean enabled = true;
    private String title = "title";
    private String description = "description";
    private String contactName = "contactName";
    private String contactUrl = "contactUrl";
    private String version = "1.0.0";

    public SwaggerConfigurationProperties() {
        // no arg constructor
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getContactUrl() {
        return this.contactUrl;
    }

    public String getVersion() {
        return this.version;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    public void setContactUrl(final String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String toString() {
        return "SwaggerConfigurationProperties(enabled=" + this.isEnabled() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", contactName=" + this.getContactName() + ", contactUrl=" + this.getContactUrl() + ", version=" + this.getVersion() + ")";
    }
}

