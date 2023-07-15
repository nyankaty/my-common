package com.company.common.spring.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.common.util.TrackingContextEnum;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Order(3)
public class RequestIdFilter extends OncePerRequestFilter {

    public RequestIdFilter() {
        // no arg constructor
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.generateCorrelationIdIfNotExists(request.getHeader(TrackingContextEnum.X_REQUEST_ID.getHeaderKey()));
        response.setHeader(TrackingContextEnum.X_REQUEST_ID.getHeaderKey(), ThreadContext.get(TrackingContextEnum.X_REQUEST_ID.getThreadKey()));
        chain.doFilter(request, response);
    }

    private void generateCorrelationIdIfNotExists(String xRequestId) {
        String requestId = StringUtils.isEmpty(xRequestId) ? UUID.randomUUID().toString().replace("-", "").toLowerCase() : xRequestId;
        ThreadContext.put(TrackingContextEnum.X_REQUEST_ID.getThreadKey(), requestId);
    }
}

