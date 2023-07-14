package com.company.common.spring.aop.annotation;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class RepositoryAspect {

    private static final Logger log = LoggerFactory.getLogger(RepositoryAspect.class);

    @Autowired
    AppConfigurationProperties appConfigurationProperties;

    public RepositoryAspect() {
        // no arg constructor
    }

    @Around("execution(* com.company.*.repository.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        String message = joinPoint.getSignature() + " exec in " + executionTime + " ms";
        if (executionTime >= (long) this.appConfigurationProperties.getRepositoryQueryLimitWarningMs()) {
            log.warn(this.appConfigurationProperties.getApplicationShortName() + " : " + message + " : SLOW QUERY");
        }

        return proceed;
    }
}
