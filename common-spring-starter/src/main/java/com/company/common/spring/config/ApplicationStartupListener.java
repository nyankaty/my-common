package com.company.common.spring.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final MainApplicationConfig mainApplicationConfig;

    public ApplicationStartupListener(MainApplicationConfig mainApplicationConfig) {
        this.mainApplicationConfig = mainApplicationConfig;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        mainApplicationConfig.logApplicationStartup();
    }
}
