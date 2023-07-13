package com.company.common.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class MainApplicationConfig {

    @Autowired
    private Environment env;

    public MainApplicationConfig() {
        // no arg constructor
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    public String getProperty(String name) {
        return this.env.getProperty(name);
    }
}
