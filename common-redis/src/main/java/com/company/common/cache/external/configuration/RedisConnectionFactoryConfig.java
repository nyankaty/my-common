package com.company.common.cache.external.configuration;

import com.company.common.cache.external.properties.RedisCacheConfigurationProperties;
import io.lettuce.core.ReadFrom;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConnectionFactoryConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConnectionFactoryConfig.class);
    RedisCacheConfigurationProperties props;

    public RedisConnectionFactoryConfig(RedisCacheConfigurationProperties props) {
        this.props = props;
    }

    @Bean
    public RedisConnectionFactory getRedisConnectionFactory() {
        if (props.isCluster() && !CollectionUtils.isEmpty(props.getNodes())) {
            return initClusterConnection();
        } else if (props.isSentinel() && !CollectionUtils.isEmpty(props.getNodes())) {
            return initSentinelConnection();
        } else {
            return initStandaloneConnection();
        }
    }

    private LettuceConnectionFactory initStandaloneConnection() {
        log.info("Redis Standalone: {}:{}", props.getHost(), props.getPort());
        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration(props.getHost(), props.getPort());
        if (StringUtils.isNotBlank(props.getUsername())) {
            redisStandaloneConfig.setUsername(props.getUsername());
        }

        if (StringUtils.isNotBlank(props.getPassword())) {
            redisStandaloneConfig.setPassword(RedisPassword.of(props.getPassword()));
        }
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private LettuceConnectionFactory initSentinelConnection() {
        log.info("Redis Sentinel: {}", props.getNodes());
        LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        Set<String> sentinelNodes = new HashSet<>(props.getNodes());
        RedisSentinelConfiguration redisSentinelConfig = new RedisSentinelConfiguration(props.getSentinelMaster(), sentinelNodes);

        if (StringUtils.isNotBlank(props.getUsername())) {
            redisSentinelConfig.setUsername(props.getUsername());
        }

        if (StringUtils.isNotBlank(props.getPassword())) {
            redisSentinelConfig.setUsername(props.getPassword());
        }
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisSentinelConfig, lettuceClientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private LettuceConnectionFactory initClusterConnection() {
        log.info("Redis Cluster: {}", props.getNodes());
        LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder().build();
        RedisClusterConfiguration redisClusterConfig = new RedisClusterConfiguration(props.getNodes());
        if (StringUtils.isNotBlank(props.getUsername())) {
            redisClusterConfig.setUsername(props.getUsername());
        }

        if (StringUtils.isNotBlank(props.getPassword())) {
            redisClusterConfig.setPassword(RedisPassword.of(props.getPassword()));
        }
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfig, lettuceClientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }
}
