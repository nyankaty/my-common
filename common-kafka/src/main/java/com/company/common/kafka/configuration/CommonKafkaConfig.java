package com.company.common.kafka.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.company.common"})
@EnableAutoConfiguration(
        exclude = {KafkaAutoConfiguration.class}
)
public class CommonKafkaConfig {

}
