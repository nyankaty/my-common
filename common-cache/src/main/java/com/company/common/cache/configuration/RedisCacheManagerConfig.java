package com.company.common.cache.configuration;

import java.util.Map;

import com.company.common.cache.customizer.RedisCacheConfigurationCustomizer;
import com.company.common.cache.customizer.RedisCacheManagerCustomizer;
import com.company.common.cache.customizer.RedisCacheWriterCustomizer;
import com.company.common.cache.customizer.RedisConnectionFactoryCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.company.common.cache.properties.RedisCacheConfigurationProperties;


@Configuration
@EnableCaching
@ComponentScan({"com.company.common"})
@ConfigurationPropertiesScan({"com.company.common.cache.properties"})
@EnableAutoConfiguration(
        exclude = {FreeMarkerAutoConfiguration.class, RedisAutoConfiguration.class, CacheAutoConfiguration.class}
)
public class RedisCacheManagerConfig {
    RedisCacheConfigurationProperties redisCacheConfigurationProperties;

    @Autowired
    public RedisCacheManagerConfig(RedisCacheConfigurationProperties redisCacheConfigurationProperties) {
        this.redisCacheConfigurationProperties = redisCacheConfigurationProperties;
    }

    @Bean
    public CacheManager redisCacheManager() {
        RedisConnectionFactory redisConnectionFactory = (new RedisConnectionFactoryCustomizer(this.redisCacheConfigurationProperties)).getRedisConnectionFactory();
        RedisCacheWriter redisCacheWriter = (new RedisCacheWriterCustomizer(redisConnectionFactory)).getRedisCacheWriter(true);
        RedisCacheConfiguration defaultRedisCacheConfiguration = (new RedisCacheConfigurationCustomizer(this.redisCacheConfigurationProperties)).getDefaultRedisCacheConfiguration();
        Map<String, RedisCacheConfiguration> redisCacheConfigurations = (new RedisCacheConfigurationCustomizer(this.redisCacheConfigurationProperties)).getRedisCacheConfigurations();
        return (new RedisCacheManagerCustomizer(defaultRedisCacheConfiguration, redisCacheConfigurations, redisCacheWriter)).getRedisCacheManager();
    }
}
