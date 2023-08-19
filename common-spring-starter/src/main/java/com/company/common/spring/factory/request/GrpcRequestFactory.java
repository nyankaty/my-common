package com.company.common.spring.factory.request;

import com.company.common.spring.config.grpc.GrpcClientInterceptor;
import com.company.common.spring.config.properties.GRpcServerPropertiesCustom;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        value = {"grpc.enabled"},
        havingValue = "true"
)
public class GrpcRequestFactory {

    private final GRpcServerPropertiesCustom gRpcServerPropertiesCustom;

    public GrpcRequestFactory(GRpcServerPropertiesCustom gRpcServerPropertiesCustom) {
        this.gRpcServerPropertiesCustom = gRpcServerPropertiesCustom;
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
