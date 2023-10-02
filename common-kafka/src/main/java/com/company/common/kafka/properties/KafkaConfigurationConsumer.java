package com.company.common.kafka.properties;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
@ConfigurationProperties(
        prefix = "spring.kafka.consumer"
)
@ConditionalOnProperty(
        value = {"spring.kafka.consumer.enabled"},
        havingValue = "true"
)
public class KafkaConfigurationConsumer extends Consumer {
    private boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return "KafkaConfigurationConsumer(enabled=" + this.isEnabled() + ")";
    }
}
