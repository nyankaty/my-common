package com.company.common.spring.config.resttemplate;

import java.util.Collections;
import com.company.common.spring.config.resttemplate.interceptor.InterceptorDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateDefault {

    private final InterceptorDefault interceptorDefault;

    public RestTemplateDefault(InterceptorDefault interceptorDefault) {
        this.interceptorDefault = interceptorDefault;
    }

    @Primary
    @Bean(name = {"restDefault"})
    public RestTemplate restTemplateDefault() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(this.interceptorDefault));
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        return restTemplate;
    }
}
