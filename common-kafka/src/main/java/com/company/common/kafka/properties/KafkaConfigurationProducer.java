package com.company.common.kafka.properties;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.kafka.producer.enabled}")
    private boolean enabled;

    @Value("${spring.kafka.producer.default-topic}")
    private String defaultTopic;

    @Value("${spring.kafka.producer.delivery-timeout}")
    private int deliveryTimeout;

    @Value("${spring.kafka.producer.request-timeout}")
    private int requestTimeout;

    @Value("${spring.kafka.producer.queue-size}")
    private int queueSize;

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
