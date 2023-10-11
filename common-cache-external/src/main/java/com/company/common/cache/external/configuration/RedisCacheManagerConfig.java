package com.company.common.cache.external.configuration;

import java.util.Map;

import com.company.common.cache.external.customizer.RedisCacheConfigurationCustomizer;
import com.company.common.cache.external.customizer.RedisCacheManagerCustomizer;
import com.company.common.cache.external.customizer.RedisCacheWriterCustomizer;
import com.company.common.cache.external.customizer.RedisConnectionFactoryCustomizer;
import com.company.common.cache.external.properties.CustomCacheExpirationsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;

@ConditionalOnProperty(
        value = {"app.cache.external.enable"},
        havingValue = "true"
)
@Configuration
@EnableCaching
@ComponentScan({"com.company.common"})
@ConfigurationPropertiesScan({"com.company.common.cache.external.properties"})
@EnableAutoConfiguration(
        exclude = {FreeMarkerAutoConfiguration.class, RedisAutoConfiguration.class, CacheAutoConfiguration.class}
)
public class RedisCacheManagerConfig {

    @Autowired(required = false)
    private RedisCacheConfigurationProperties externalCacheProp;
    @Autowired(required = false)
    private CustomCacheExpirationsProperties expirationsProp;

    @Bean
    public CacheManager redisCacheManager() {
        RedisConnectionFactory redisConnectionFactory = (new RedisConnectionFactoryCustomizer(this.externalCacheProp)).getRedisConnectionFactory();
        RedisCacheWriter redisCacheWriter = (new RedisCacheWriterCustomizer(redisConnectionFactory)).getRedisCacheWriter(true);
        RedisCacheConfiguration defaultRedisCacheConfiguration = (new RedisCacheConfigurationCustomizer(this.externalCacheProp, this.expirationsProp)).getDefaultRedisCacheConfiguration();
        Map<String, RedisCacheConfiguration> redisCacheConfigurations = (new RedisCacheConfigurationCustomizer(this.externalCacheProp, this.expirationsProp)).getRedisCacheConfigurations();
        return (new RedisCacheManagerCustomizer(defaultRedisCacheConfiguration, redisCacheConfigurations, redisCacheWriter)).getRedisCacheManager();
    }
}
