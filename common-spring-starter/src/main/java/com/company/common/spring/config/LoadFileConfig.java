package com.company.common.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:config.properties"})
public class LoadFileConfig {

    public LoadFileConfig() {
        // no arg constructor
    }
}
