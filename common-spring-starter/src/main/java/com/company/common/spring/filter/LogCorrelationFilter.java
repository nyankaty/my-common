package com.company.common.spring.filter;

import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import com.company.common.util.enums.TrackingContextEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Order(1)
public class LogCorrelationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LogCorrelationFilter.class);

    private final AppConfigurationProperties appConfig;

    public LogCorrelationFilter(AppConfigurationProperties appConfig) {
        this.appConfig = appConfig;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            long time = System.currentTimeMillis();
            this.generateCorrelationIdIfNotExists(request.getHeader(TrackingContextEnum.X_CORRELATION_ID.getHeaderKey()));
            response.setHeader(TrackingContextEnum.X_CORRELATION_ID.getHeaderKey(), ThreadContext.get(TrackingContextEnum.X_CORRELATION_ID.getThreadKey()));
            chain.doFilter(request, response);
            log.info("{}: {} ms ", request.getRequestURI(), System.currentTimeMillis() - time);
            ThreadContext.clearAll();
        } catch (Exception e) {
            log.error("[FILTER]: An error occur: {}", e.getMessage());
        }
    }

    private void generateCorrelationIdIfNotExists(String xCorrelationId) {
        String uuId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String nameWhenNull = (this.appConfig.getApplicationShortName() + "-" + uuId).trim();
        String correlationId = StringUtils.isEmpty(xCorrelationId) ? nameWhenNull : xCorrelationId;
        ThreadContext.put(TrackingContextEnum.X_CORRELATION_ID.getThreadKey(), correlationId);
    }
}
