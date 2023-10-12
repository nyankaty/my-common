package com.company.common.cache.external.configuration;

import java.util.Map;

import com.company.common.cache.external.customizer.RedisCacheConfigurationCustomizer;
import com.company.common.cache.external.customizer.RedisCacheManagerCustomizer;
import com.company.common.cache.external.customizer.RedisCacheWriterCustomizer;
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
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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

    private RedisCacheConfigurationProperties props;
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisCacheManagerConfig(RedisCacheConfigurationProperties props, RedisConnectionFactory redisConnectionFactory) {
        this.props = props;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheWriter redisCacheWriter = (new RedisCacheWriterCustomizer(redisConnectionFactory)).getRedisCacheWriter(true);
        RedisCacheConfiguration defaultRedisCacheConfiguration = (new RedisCacheConfigurationCustomizer(props)).getDefaultRedisCacheConfiguration();
        Map<String, RedisCacheConfiguration> redisCacheConfigurations = (new RedisCacheConfigurationCustomizer(props)).getRedisCacheConfigurations();
        return (new RedisCacheManagerCustomizer(defaultRedisCacheConfiguration, redisCacheConfigurations, redisCacheWriter)).getRedisCacheManager();
    }

    @Bean({"defaultRedisTemplate"})
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
