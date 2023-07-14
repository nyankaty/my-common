package com.company.common.util;

public final class ConstantPropertiesConfig {
    public static final String APP_LOG_REQUEST_HTTP = "app.log-request-http";
    public static final String APP_CACHE_MEMORY_ENABLE = "app.cache.memory.enable";
    public static final String APP_CACHE_MEMORY = "app.cache.memory";
    public static final String APP_CACHE_REDIS_ENABLE = "app.cache.redis.enable";
    public static final String APP_CACHE_REDIS = "app.cache.redis";
    public static final String REST_DEFAULT_SERVICE_ENABLE_LOG_REQUEST = "app.default-service-enable-log-request";
    public static final String APPLICATION_SHORT_NAME = "app.application-short-name";
    public static final String APPLICATION_CONTEXT_NAME = "app.application-context-name";
    public static final String APP_REPOSITORY_QUERY_LIMIT_WARNING_MS = "app.repository-query-limit-warning-ms";
    public static final String SPRING_KAFKA_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";
    public static final String SPRING_KAFKA_ENABLED = "spring.kafka.enabled";
    public static final String APP_AUTH = "app.auth";
    public static final String APP_AUTH_ENABLED = "app.auth.enabled";
    public static final String APP_AUTH_KEY_PUBLIC_KEY = "app.auth.key.public-key";
    public static final String APP_AUTH_TYPE = "app.auth.type";
    public static final String SPRING_KAFKA_CONSUMER_ENABLED = "spring.kafka.consumer.enabled";
    public static final String SPRING_KAFKA_PRODUCER_ENABLED = "spring.kafka.producer.enabled";
    public static final String VALIDATION_CODE = "app.validation-code";
    public static final String GRPC_CLIENT_REQUEST_TIMEOUT_MS = "grpc.client-request-timeout-ms=";
    public static final String GRPC_CLIENT_REQUEST_LOG = "grpc.client-request-log";
    public static final String GRPC_SERVER_LOG = "grpc.server-log";
    public static final String INFO_BUILD = "info.build";
    public static final String INFO_BUILD_ARTIFACT = "info.build.artifact";
    public static final String INFO_BUILD_NAME = "info.build.name";
    public static final String INFO_BUILD_DESCRIPTION = "info.build.description";
    public static final String INFO_BUILD_VERSION = "info.build.version";
    public static final String LOG_GRAPHQL_ENABLED = "app.log-graphql-enabled";
    public static final String TIME_TRACE_ENABLED = "app.time-trace-enabled";

    private ConstantPropertiesConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
