package com.company.common.redis.configuration;

import com.company.common.redis.properties.RedisConfigurationProperties;
import io.lettuce.core.ReadFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashSet;
import java.util.Set;

@ConditionalOnProperty(
        value = {"spring.redis.enabled"},
        havingValue = "true"
)
@Configuration
public class RedisConnectionFactoryConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConnectionFactoryConfig.class);
    private final RedisConfigurationProperties props;

    public RedisConnectionFactoryConfig(RedisConfigurationProperties props) {
        this.props = props;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        if (props.getCluster() != null) {
            return initClusterConnection();
        } else if (props.getSentinel() != null) {
            return initSentinelConnection();
        } else {
            return initStandaloneConnection();
        }
    }

    private LettuceConnectionFactory initStandaloneConnection() {
        log.info("Redis Standalone: {}:{}", props.getHost(), props.getPort());
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(props.getHost(), props.getPort());

        standaloneConfig.setUsername(props.getUsername());
        standaloneConfig.setPassword(RedisPassword.of(props.getPassword()));

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(standaloneConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private LettuceConnectionFactory initSentinelConnection() {
        log.info("Redis Sentinel: {}", props.getSentinel().getNodes());
        LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        Set<String> sentinelNodes = new HashSet<>(props.getSentinel().getNodes());
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(props.getSentinel().getMaster(), sentinelNodes);
        sentinelConfig.setSentinelUsername(props.getSentinel().getUsername());
        sentinelConfig.setSentinelPassword(RedisPassword.of(props.getSentinel().getPassword()));

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(sentinelConfig, lettuceClientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private LettuceConnectionFactory initClusterConnection() {
        log.info("Redis Cluster: {}", props.getCluster().getNodes());
        LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder().build();

        RedisClusterConfiguration redisClusterConfig = new RedisClusterConfiguration(props.getCluster().getNodes());
        redisClusterConfig.setUsername(props.getUsername());
        redisClusterConfig.setPassword(RedisPassword.of(props.getPassword()));

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfig, lettuceClientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }
}
