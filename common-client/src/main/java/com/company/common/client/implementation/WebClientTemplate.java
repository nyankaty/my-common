package com.company.common.client.implementation;

import com.company.common.client.application.ClientTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.function.Consumer;

public class WebClientTemplate implements ClientTemplate {

    private static final Logger log = LoggerFactory.getLogger(WebClientTemplate.class);

    public WebClientTemplate() {
        // no arg constructor
    }

    public <R> Flux<R> get(String uri, Consumer<HttpHeaders> headersConsumer, int timeOutSeconds, Class<R> responseClass, Consumer<? super R> consumer) {
        log.info("Start non-blocking GET uri: {}", uri);
        Flux<R> flux = WebClient.create().get().uri(uri).headers(headersConsumer)
                .retrieve().bodyToFlux(responseClass)
                .timeout(Duration.ofSeconds(timeOutSeconds));
        if (consumer != null) {
            flux.subscribe(consumer);
        }

        log.info("Finish non-blocking GET uri: {}", uri);
        return flux;
    }

    public <B, R> Flux<R> post(String uri, Consumer<HttpHeaders> headersConsumer, int timeOutSeconds, B body, Class<B> bodyClass, Class<R> responseClass, Consumer<? super R> consumer) {
        log.info("Start non-blocking POST uri: {}", uri);
        Flux<R> flux = WebClient.create().post().uri(uri).headers(headersConsumer).body(Flux.just(body), bodyClass)
                .retrieve().bodyToFlux(responseClass)
                .timeout(Duration.ofSeconds(timeOutSeconds));
        if (consumer != null) {
            flux.subscribe(consumer);
        }

        log.info("Finish non-blocking POST uri: {}", uri);
        return flux;
    }

    public <R> R get(String uri, HttpHeaders headers, int timeOutSeconds, Class<R> responseClass) {
        log.info("Start blocking GET uri: {}", uri);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeOutSeconds * 1000);
        httpRequestFactory.setReadTimeout(timeOutSeconds * 1000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        HttpEntity<R> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, request, responseClass);
        return response.getBody();
    }

    public <B, R> R post(String uri, HttpHeaders headers, int timeOutSeconds, B body, Class<R> responseClass) {
        log.info("Start blocking POST uri: {}", uri);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeOutSeconds * 1000);
        httpRequestFactory.setReadTimeout(timeOutSeconds * 1000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpEntity<B> request = new HttpEntity<>(body, headers);
        ResponseEntity<R> response = restTemplate.postForEntity(builder.build().toUri(), request, responseClass);
        return response.getBody();
    }
}