package com.company.common.redis.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConditionalOnProperty(
        value = {"spring.redis.enabled"},
        havingValue = "true"
)
@Configuration
@Primary
@ConfigurationProperties(
        prefix = "spring.redis"
)
public class RedisConfigurationProperties extends RedisProperties {

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
