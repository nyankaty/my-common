package com.company.common.kafka.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
@ConfigurationProperties(
        prefix = "spring.kafka.producer"
)
@ConditionalOnProperty(
        value = {"spring.kafka.producer.enabled"},
        havingValue = "true"
)
public class KafkaConfigurationProducer extends Producer {

    private boolean enabled;

    private String defaultTopic;

    private int deliveryTimeout = 5000;

    private int requestTimeout = 3000;

    private int queueSize = 100;

    public KafkaConfigurationProducer() {
        // no arg constructor
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getDefaultTopic() {
        return this.defaultTopic;
    }

    public int getDeliveryTimeout() {
        return this.deliveryTimeout;
    }

    public int getRequestTimeout() {
        return this.requestTimeout;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public void setDeliveryTimeout(int deliveryTimeout) {
        this.deliveryTimeout = deliveryTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public String toString() {
        return "KafkaConfigurationProducer(enabled=" + this.isEnabled() + ", defaultTopic=" + this.getDefaultTopic() + ", deliveryTimeout=" + this.getDeliveryTimeout() + ", requestTimeout=" + this.getRequestTimeout() + ", queueSize=" + this.getQueueSize() + ")";
    }
}
