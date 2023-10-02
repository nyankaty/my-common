package com.company.common.spring;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.company.common"})
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class, RedisAutoConfiguration.class, CacheAutoConfiguration.class}
)
public class AppConfigArch {

}
