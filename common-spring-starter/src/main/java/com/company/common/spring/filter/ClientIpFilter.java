package com.company.common.spring.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.company.common.util.enums.TrackingContextEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Order(2)
public class ClientIpFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ClientIpFilter.class);

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            String xRealIp = request.getHeader(TrackingContextEnum.X_REAL_IP.getHeaderKey());
            String ipList = StringUtils.isEmpty(xRealIp) ? request.getRemoteAddr() : xRealIp;
            ThreadContext.put(TrackingContextEnum.X_REAL_IP.getThreadKey(), ipList);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("[FILTER]: An error occur: {}", e.getMessage());
        }
    }
}
