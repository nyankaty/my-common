package com.company.common.spring.config.resttemplate.interceptor;

import java.io.IOException;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import com.company.common.spring.config.resttemplate.UtilsRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Configuration
public class InterceptorDefault implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(InterceptorDefault.class);
    final AppConfigurationProperties appConfigurationProperties;
    private final UtilsRestTemplate utilsRestTemplate;

    @Autowired
    public InterceptorDefault(UtilsRestTemplate utilsRestTemplate, AppConfigurationProperties appConfigurationProperties) {
        this.utilsRestTemplate = utilsRestTemplate;
        this.appConfigurationProperties = appConfigurationProperties;
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            HttpHeaders headers = request.getHeaders();
            this.utilsRestTemplate.addHeader(headers, "Accept-Language", "vi");
        } catch (Exception var5) {
            log.error("Interceptor Rest error : ", var5);
        }

        ClientHttpResponse httpResponse = execution.execute(request, body);
        if (this.appConfigurationProperties.isDefaultServiceEnableLogRequest()) {
            this.utilsRestTemplate.trace(request, body, httpResponse);
        }

        return httpResponse;
    }
}
