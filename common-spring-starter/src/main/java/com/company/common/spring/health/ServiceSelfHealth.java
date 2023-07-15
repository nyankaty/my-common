package com.company.common.spring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServiceSelfHealth implements HealthIndicator {
    public ServiceSelfHealth() {
        // no arg constructor
    }

    public Health health() {
        return Health.up().withDetail("service_status", "available").build();
    }
}
