package com.company.common.cache.internal.configuration;

import com.company.common.cache.internal.properties.CaffeineCacheConfigurationProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ConditionalOnProperty(
        value = {"app.cache.internal.enable"},
        havingValue = "true"
)
@Configuration
@ConfigurationPropertiesScan({"com.company.common.cache.internal.configuration"})
@EnableAutoConfiguration(
        exclude = {FreeMarkerAutoConfiguration.class}
)
public class CaffeineConfig {

    private static final Logger log = LoggerFactory.getLogger(CaffeineConfig.class);

    CaffeineCacheConfigurationProperties properties;

    RemovalListener<Object, Object> removalListener = (o, o2, removalCause) -> {
        assert o != null;

        assert o2 != null;

        log.info("Remove {} value {} removalClause {}", o, o2, removalCause);
    };

    @Autowired
    public CaffeineConfig(CaffeineCacheConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Primary
    public Caffeine<Object, Object> caffeine() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        if (this.properties.getExpireAfterWrite() != null) {
            caffeine.expireAfterWrite(this.properties.getExpireAfterWrite(), TimeUnit.SECONDS);
        }

        if (this.properties.getExpireAfterAccess() != null) {
            caffeine.expireAfterAccess(this.properties.getExpireAfterAccess(), TimeUnit.SECONDS);
        }

        if (this.properties.getCacheTemporaryDisable()) {
            caffeine.maximumSize(0L);
        }

        if (this.properties.getEnableRecordStats()) {
            caffeine.recordStats();
        }

        if (this.properties.getRemovalListener()) {
            caffeine.removalListener(this.removalListener);
        }

        return caffeine;
    }
}

