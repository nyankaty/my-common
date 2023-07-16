package com.company.common.spring.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.common.spring.constant.TrackingContextEnum;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Order(2)
public class ClientIpFilter extends OncePerRequestFilter {

    public ClientIpFilter() {
        // no arg constructor
    }

    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String xRealIp = httpServletRequest.getHeader(TrackingContextEnum.X_REAL_IP.getHeaderKey());
        String ipList = StringUtils.isEmpty(xRealIp) ? httpServletRequest.getRemoteAddr() : xRealIp;
        ThreadContext.put(TrackingContextEnum.X_REAL_IP.getThreadKey(), ipList);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
