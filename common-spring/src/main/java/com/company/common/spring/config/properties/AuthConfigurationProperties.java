package com.company.common.spring.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConfigurationProperties(
        prefix = "app.auth"
)
@Configuration
@Primary
@RefreshScope
@ConditionalOnProperty(
        value = {"app.auth.enabled"},
        havingValue = "true"
)
public class AuthConfigurationProperties {

    private boolean enabled;

    @Value("${app.auth.type}")
    private AuthConfigurationProperties.Type type;

    @Autowired(required = false)
    private AuthConfigurationProperties.AuthTypePublicKey key;

    public AuthConfigurationProperties() {
        // no arg constructor
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public AuthConfigurationProperties.Type getType() {
        return this.type;
    }

    public AuthConfigurationProperties.AuthTypePublicKey getKey() {
        return this.key;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setType(final AuthConfigurationProperties.Type type) {
        this.type = type;
    }

    public void setKey(final AuthConfigurationProperties.AuthTypePublicKey key) {
        this.key = key;
    }

    public String toString() {
        return "AuthConfigurationProperties(enabled=" + this.isEnabled() + ", type=" + this.getType() + ", key=" + this.getKey() + ")";
    }

    @Configuration
    @ConditionalOnProperty(
            value = {"app.auth.type"},
            havingValue = "KEY"
    )
    @RefreshScope
    public static class AuthTypePublicKey {
        @Value("${app.auth.key.public-key}")
        private String publicKey;

        public AuthTypePublicKey() {
            // no arg constructor
        }

        public String getPublicKey() {
            return this.publicKey;
        }

        public void setPublicKey(final String publicKey) {
            this.publicKey = publicKey;
        }

        public String toString() {
            return "AuthConfigurationProperties.AuthTypePublicKey(publicKey=" + this.getPublicKey() + ")";
        }
    }

    public enum Type {
        KEY;

        Type() {}
    }
}

