package com.company.common.cache.external.customizer;

import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class RedisConnectionFactoryCustomizer {
    private static final Logger log = LoggerFactory.getLogger(RedisConnectionFactoryCustomizer.class);
    RedisCacheConfigurationProperties properties;

    public RedisConnectionFactoryCustomizer(RedisCacheConfigurationProperties properties) {
        this.properties = properties;
    }

    public RedisConnectionFactory getRedisConnectionFactory() {
        if (!CollectionUtils.isEmpty(this.properties.getNodes())) {
            log.info("Redis Cluster: {}", this.properties.getNodes());
            JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder().build();
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(this.properties.getNodes());
            if (!StringUtils.isEmpty(this.properties.getPassword())) {
                redisClusterConfiguration.setPassword(RedisPassword.of(this.properties.getPassword()));
            }

            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, clientConfiguration);
            jedisConnectionFactory.afterPropertiesSet();
            return jedisConnectionFactory;
        } else {
            log.info("Redis Standalone: {}:{}", this.properties.getHost(), this.properties.getPort());
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(this.properties.getHost());
            redisStandaloneConfiguration.setPort(this.properties.getPort());
            if (!StringUtils.isEmpty(this.properties.getPassword())) {
                redisStandaloneConfiguration.setPassword(RedisPassword.of(this.properties.getPassword()));
            }

            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
            jedisConnectionFactory.afterPropertiesSet();
            return jedisConnectionFactory;
        }
    }
}
