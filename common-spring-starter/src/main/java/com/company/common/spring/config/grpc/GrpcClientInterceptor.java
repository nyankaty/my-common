package com.company.common.spring.config.grpc;

import com.company.common.spring.config.properties.GRpcServerPropertiesCustom;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.ClientCall.Listener;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class GrpcClientInterceptor implements ClientInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcClientInterceptor.class);
    private final String host;
    private final int port;
    private final GRpcServerPropertiesCustom gRpcServerPropertiesCustom;

    public <M, R> ClientCall<M, R> interceptCall(MethodDescriptor<M, R> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new SimpleForwardingClientCall<M, R>(channel.newCall(methodDescriptor, callOptions.withDeadlineAfter(this.gRpcServerPropertiesCustom.getClientRequestTimeoutMs(), TimeUnit.MILLISECONDS))) {
            @Override
            public void sendMessage(M message) {
                try {
                    if (GrpcClientInterceptor.this.gRpcServerPropertiesCustom.isClientRequestLog()) {
                        GrpcClientInterceptor.log.info("Server request [:{}:{}] with method [{}] and body:\n{}", GrpcClientInterceptor.this.host, GrpcClientInterceptor.this.port, methodDescriptor.getFullMethodName(), message);
                    }

                    super.sendMessage(message);
                } catch (Throwable var3) {
                    throw var3;
                }
            }

            @Override
            public void start(Listener<R> responseListener, Metadata headers) {
                GrpcClientInterceptor.GrpcClientListener<R> grpcClientListener = new GrpcClientInterceptor.GrpcClientListener<>(GrpcClientInterceptor.this.host, GrpcClientInterceptor.this.port, methodDescriptor.getFullMethodName(), System.currentTimeMillis(), GrpcClientInterceptor.this.gRpcServerPropertiesCustom, responseListener);
                super.start(grpcClientListener, headers);
            }
        };
    }

    public GrpcClientInterceptor(final String host, final int port, final GRpcServerPropertiesCustom gRpcServerPropertiesCustom) {
        this.host = host;
        this.port = port;
        this.gRpcServerPropertiesCustom = gRpcServerPropertiesCustom;
    }

    private static class GrpcClientListener<R> extends Listener<R> {
        private static final Logger log = LoggerFactory.getLogger(GrpcClientInterceptor.GrpcClientListener.class);
        String methodName;
        Listener<R> responseListener;
        private final String host;
        private final int port;
        private final Long timeStartRequestMs;
        private final GRpcServerPropertiesCustom gRpcServerPropertiesCustom;

        protected GrpcClientListener(String host, int port, String methodName, Long timeStartRequestMs, GRpcServerPropertiesCustom gRpcServerPropertiesCustom, Listener<R> responseListener) {
            this.host = host;
            this.port = port;
            this.methodName = methodName;
            this.timeStartRequestMs = timeStartRequestMs;
            this.responseListener = responseListener;
            this.gRpcServerPropertiesCustom = gRpcServerPropertiesCustom;
        }

        @Override
        public void onMessage(R message) {
            try {
                if (this.gRpcServerPropertiesCustom.isClientRequestLog()) {
                    log.info("Server Response [:{}:{}] with method [{}] with response ms: {} Response:\n{}", this.host, this.port, this.methodName, System.currentTimeMillis() - this.timeStartRequestMs, message);
                }

                this.responseListener.onMessage(message);
            } catch (Throwable var3) {
                throw var3;
            }
        }

        @Override
        public void onHeaders(Metadata headers) {
            this.responseListener.onHeaders(headers);
        }

        @Override
        public void onClose(Status status, Metadata trailers) {
            this.responseListener.onClose(status, trailers);
        }

        @Override
        public void onReady() {
            this.responseListener.onReady();
        }
    }
}

