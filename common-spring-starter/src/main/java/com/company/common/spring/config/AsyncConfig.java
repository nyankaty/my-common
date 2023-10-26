package com.company.common.spring.config;

import com.company.common.spring.config.properties.AsyncConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@ConditionalOnProperty(
        value = {"app.executor.enabled"},
        havingValue = "true"
)
@EnableConfigurationProperties(AsyncConfigurationProperties.class)
@Configuration
@EnableAsync
public class AsyncConfig {
    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);
    private final AsyncConfigurationProperties asyncConfigurationProperties;

    public AsyncConfig(AsyncConfigurationProperties asyncConfigurationProperties) {
        this.asyncConfigurationProperties = asyncConfigurationProperties;
    }

    @Bean({"threadPoolTaskExecutor"})
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.asyncConfigurationProperties.getAsyncExecutorCorePoolSize());
        executor.setMaxPoolSize(this.asyncConfigurationProperties.getAsyncExecutorMaxPoolSize());
        executor.setKeepAliveSeconds(this.asyncConfigurationProperties.getKeepAliveSeconds());
        executor.setQueueCapacity(this.asyncConfigurationProperties.getQueueCapacity());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix(this.asyncConfigurationProperties.getAsyncExecutorThreadNamePrefix());

        log.info("Configured Async bean name: 'threadPoolTaskExecutor', corePoolSize = {}, maxPoolSize = {}, keepAliveSeconds = {}, queueCapacity = {}, waitForTasksToCompleteOnShutdown = true",
                this.asyncConfigurationProperties.getAsyncExecutorCorePoolSize(),
                this.asyncConfigurationProperties.getAsyncExecutorMaxPoolSize(),
                this.asyncConfigurationProperties.getKeepAliveSeconds(),
                this.asyncConfigurationProperties.getQueueCapacity());

        return executor;
    }
}
