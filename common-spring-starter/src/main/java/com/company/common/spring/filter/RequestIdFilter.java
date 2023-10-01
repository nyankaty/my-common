package com.company.common.spring.filter;

import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.company.common.util.enums.TrackingContextEnum;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Order(3)
public class RequestIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestIdFilter.class);

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            this.generateCorrelationIdIfNotExists(request.getHeader(TrackingContextEnum.X_REQUEST_ID.getHeaderKey()));
            response.setHeader(TrackingContextEnum.X_REQUEST_ID.getHeaderKey(), ThreadContext.get(TrackingContextEnum.X_REQUEST_ID.getThreadKey()));
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("[FILTER]: An error occur: {}", e.getMessage());
        }
    }

    private void generateCorrelationIdIfNotExists(String xRequestId) {
        String requestId = StringUtils.isEmpty(xRequestId) ? UUID.randomUUID().toString().replace("-", "").toLowerCase() : xRequestId;
        ThreadContext.put(TrackingContextEnum.X_REQUEST_ID.getThreadKey(), requestId);
    }
}

