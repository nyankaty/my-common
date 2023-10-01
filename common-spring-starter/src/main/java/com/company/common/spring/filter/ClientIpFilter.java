package com.company.common.spring.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
@Order(2)
public class ClientIpFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ClientIpFilter.class);

    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String xRealIp = httpServletRequest.getHeader(TrackingContextEnum.X_REAL_IP.getHeaderKey());
            String ipList = StringUtils.isEmpty(xRealIp) ? httpServletRequest.getRemoteAddr() : xRealIp;
            ThreadContext.put(TrackingContextEnum.X_REAL_IP.getThreadKey(), ipList);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            log.error("[FILTER]: An error occur: {}", e.getMessage());
        }
    }
}
