package com.company.common.cache.external.customizer;

import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import io.lettuce.core.ReadFrom;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

public class RedisConnectionFactoryCustomizer {

    private static final Logger log = LoggerFactory.getLogger(RedisConnectionFactoryCustomizer.class);
    RedisCacheConfigurationProperties properties;

    public RedisConnectionFactoryCustomizer(RedisCacheConfigurationProperties properties) {
        this.properties = properties;
    }

    public RedisConnectionFactory getRedisConnectionFactory() {
        if (properties.isCluster() && !CollectionUtils.isEmpty(this.properties.getNodes())) {
            log.info("Redis Cluster: {}", this.properties.getNodes());
            LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder().build();
            RedisClusterConfiguration redisClusterConfig = new RedisClusterConfiguration(this.properties.getNodes());
            if (StringUtils.isNotBlank(this.properties.getUsername())) {
                redisClusterConfig.setUsername(this.properties.getUsername());
            }

            if (StringUtils.isNotBlank(this.properties.getPassword())) {
                redisClusterConfig.setPassword(RedisPassword.of(this.properties.getPassword()));
            }
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfig, lettuceClientConfig);
            lettuceConnectionFactory.afterPropertiesSet();
            return lettuceConnectionFactory;
        } else if (properties.isSentinel() && !CollectionUtils.isEmpty(this.properties.getNodes())) {
            log.info("Redis Sentinel: {}", this.properties.getNodes());
            LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder()
                    .readFrom(ReadFrom.REPLICA_PREFERRED)
                    .build();
            Set<String> sentinelNodes = new HashSet<>(this.properties.getNodes());
            RedisSentinelConfiguration redisSentinelConfig = new RedisSentinelConfiguration(properties.getSentinelMaster(), sentinelNodes);

            if (StringUtils.isNotBlank(this.properties.getUsername())) {
                redisSentinelConfig.setUsername(this.properties.getUsername());
            }

            if (StringUtils.isNotBlank(this.properties.getPassword())) {
                redisSentinelConfig.setUsername(this.properties.getPassword());
            }
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisSentinelConfig, lettuceClientConfig);
            lettuceConnectionFactory.afterPropertiesSet();
            return lettuceConnectionFactory;
        } else {
            log.info("Redis Standalone: {}:{}", this.properties.getHost(), this.properties.getPort());
            RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
            redisStandaloneConfig.setHostName(this.properties.getHost());
            redisStandaloneConfig.setPort(this.properties.getPort());
            if (StringUtils.isNotBlank(this.properties.getUsername())) {
                redisStandaloneConfig.setUsername(this.properties.getUsername());
            }

            if (StringUtils.isNotBlank(this.properties.getPassword())) {
                redisStandaloneConfig.setPassword(RedisPassword.of(this.properties.getPassword()));
            }
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfig);
            lettuceConnectionFactory.afterPropertiesSet();
            return lettuceConnectionFactory;
        }
    }
}
