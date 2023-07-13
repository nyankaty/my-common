package com.company.common.client.application;

import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import java.util.function.Consumer;

public interface ClientTemplate {
    <R> Flux<R> get(String var1, Consumer<HttpHeaders> var2, int var3, Class<R> var4, Consumer<? super R> var5);

    <B, R> Flux<R> post(String var1, Consumer<HttpHeaders> var2, int var3, B var4, Class<B> var5, Class<R> var6, Consumer<? super R> var7);

    <R> R get(String var1, HttpHeaders var2, int var3, Class<R> var4);

    <B, R> R post(String var1, HttpHeaders var2, int var3, B var4, Class<R> var5);
}