package com.company.common.spring.aop.annotation;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class RepositoryAspect {

    private static final Logger log = LoggerFactory.getLogger(RepositoryAspect.class);

    private final AppConfigurationProperties appConfigurationProperties;

    public RepositoryAspect(AppConfigurationProperties appConfigurationProperties) {
        this.appConfigurationProperties = appConfigurationProperties;
    }

    @Around("execution(* com.company.*.repository.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        String message = joinPoint.getSignature() + " exec in " + executionTime + " ms";
        if (executionTime >= this.appConfigurationProperties.getRepositoryQueryLimitWarningMs()) {
            log.warn("{} : {} : SLOW QUERY", this.appConfigurationProperties.getApplicationShortName(), message);
        }

        return proceed;
    }
}
