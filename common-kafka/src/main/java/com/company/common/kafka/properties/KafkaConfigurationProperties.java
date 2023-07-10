package com.company.common.kafka.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConfigurationProperties(
        prefix = "spring.kafka"
)
@Configuration
@Primary
@ConditionalOnProperty(
        value = {"spring.kafka.enabled"},
        havingValue = "true"
)
public class KafkaConfigurationProperties extends KafkaProperties {

    private boolean enabled;

    public KafkaConfigurationProperties() {
        // no arg constructor
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return "KafkaConfigurationProperties(enabled=" + this.isEnabled() + ")";
    }
}
