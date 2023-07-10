package com.company.common.kafka.configuration;

import com.company.common.kafka.properties.KafkaConfigurationProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(
        value = {"spring.kafka.producer.enabled"},
        havingValue = "true"
)
public class KafkaProducerEventConfig {
    final KafkaConfigurationProducer kafkaConfigurationProducer;

    public KafkaProducerEventConfig(KafkaConfigurationProducer kafkaConfigurationProducer) {
        this.kafkaConfigurationProducer = kafkaConfigurationProducer;
    }

    @Bean({"kafkaEventProducerFactory"})
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", this.kafkaConfigurationProducer.getBootstrapServers());
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        props.put("retries", this.kafkaConfigurationProducer.getRetries());
        props.put("delivery.timeout.ms", this.kafkaConfigurationProducer.getDeliveryTimeout());
        props.put("request.timeout.ms", this.kafkaConfigurationProducer.getRequestTimeout());
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new StringSerializer());
    }

    @Bean({"defaultKafkaTemplate"})
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(this.producerFactory());
    }

    public KafkaConfigurationProducer getKafkaConfigurationProducer() {
        return this.kafkaConfigurationProducer;
    }
}
