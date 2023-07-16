package com.company.common.spring.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import com.company.common.spring.constant.TrackingContextEnum;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Order(1)
public class LogCorrelationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LogCorrelationFilter.class);

    @Autowired
    private AppConfigurationProperties appConfig;

    public LogCorrelationFilter() {
        // no arg constructor
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        long time = System.currentTimeMillis();
        this.generateCorrelationIdIfNotExists(request.getHeader(TrackingContextEnum.X_CORRELATION_ID.getHeaderKey()));
        response.setHeader(TrackingContextEnum.X_CORRELATION_ID.getHeaderKey(), ThreadContext.get(TrackingContextEnum.X_CORRELATION_ID.getThreadKey()));
        chain.doFilter(request, response);
        log.info("{}: {} ms ", request.getRequestURI(), System.currentTimeMillis() - time);
        ThreadContext.clearAll();
    }

    private void generateCorrelationIdIfNotExists(String xCorrelationId) {
        String uuId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String nameWhenNull = (this.appConfig.getApplicationShortName() + "-" + uuId).trim();
        String correlationId = StringUtils.isEmpty(xCorrelationId) ? nameWhenNull : xCorrelationId;
        ThreadContext.put(TrackingContextEnum.X_CORRELATION_ID.getThreadKey(), correlationId);
    }
}
