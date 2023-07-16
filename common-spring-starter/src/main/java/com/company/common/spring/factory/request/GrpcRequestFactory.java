package com.company.common.spring.factory.request;

import com.company.common.spring.config.grpc.GrpcClientInterceptor;
import com.company.common.spring.config.properties.GRpcServerPropertiesCustom;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcRequestFactory {

    @Autowired
    GRpcServerPropertiesCustom gRpcServerPropertiesCustom;

    public GrpcRequestFactory() {
        // no arg constructor
    }

    public ManagedChannel createChannel(String host, int port) {
        GrpcClientInterceptor grpcClientInterceptor = new GrpcClientInterceptor(host, port, this.gRpcServerPropertiesCustom);
        return (NettyChannelBuilder.forAddress(host, port).usePlaintext().intercept(grpcClientInterceptor)).build();
    }

    public ManagedChannel createChannel(String host, int port, SslContext sslContext) {
        if (sslContext != null) {
            GrpcClientInterceptor grpcClientInterceptor = new GrpcClientInterceptor(host, port, this.gRpcServerPropertiesCustom);
            return (NettyChannelBuilder.forAddress(host, port).sslContext(sslContext).intercept(grpcClientInterceptor)).build();
        } else {
            return this.createChannel(host, port);
        }
    }
}
