package com.company.common.spring.config.grpc;

import com.company.common.spring.config.properties.GRpcServerPropertiesCustom;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata.Key;
import io.grpc.ServerCall.Listener;
import org.apache.logging.log4j.ThreadContext;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@GRpcGlobalInterceptor
@Configuration
@ConditionalOnProperty(
        value = {"grpc.enabled"},
        havingValue = "true"
)
@SuppressWarnings("java:S1192")
public class GrpcServerInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcServerInterceptor.class);
    private static final Key<String> REQUEST_ID_KEY;

    @Autowired
    GRpcServerPropertiesCustom gRpcServerPropertiesCustom;

    public GrpcServerInterceptor() {
        // no arg constructor
    }

    public <M, R> Listener<M> interceptCall(ServerCall<M, R> serverCall, Metadata metadata, ServerCallHandler<M, R> serverCallHandler) {
        final String requestId = metadata.get(REQUEST_ID_KEY);
        ThreadContext.put("X-Request-Id", requestId);
        GrpcServerInterceptor.GrpcServerCall<M, R> grpcServerCall = new GrpcServerInterceptor.GrpcServerCall<>(serverCall, this.gRpcServerPropertiesCustom);
        Listener<M> listener = serverCallHandler.startCall(grpcServerCall, metadata);
        return new SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onMessage(M message) {
                if (GrpcServerInterceptor.this.gRpcServerPropertiesCustom.isServerLog()) {
                    GrpcServerInterceptor.log.info("[{}] Request (Request id {}) : body request: \n{}", serverCall.getMethodDescriptor().getFullMethodName(), requestId, message);
                }

                super.onMessage(message);
            }
        };
    }

    static {
        REQUEST_ID_KEY = Key.of("X-Request-Id", Metadata.ASCII_STRING_MARSHALLER);
    }

    private static class GrpcServerCall<M, R> extends ServerCall<M, R> {
        private static final Logger log = LoggerFactory.getLogger(GrpcServerInterceptor.GrpcServerCall.class);
        GRpcServerPropertiesCustom gRpcServerPropertiesCustom;
        ServerCall<M, R> serverCall;

        protected GrpcServerCall(ServerCall<M, R> serverCall, GRpcServerPropertiesCustom gRpcServerPropertiesCustom) {
            this.serverCall = serverCall;
            this.gRpcServerPropertiesCustom = gRpcServerPropertiesCustom;
        }

        public void request(int numMessages) {
            this.serverCall.request(numMessages);
        }

        public void sendHeaders(Metadata headers) {
            this.serverCall.sendHeaders(headers);
        }

        public void sendMessage(R message) {
            if (this.gRpcServerPropertiesCustom.isServerLog()) {
                log.info("[{}] Response form server to client:\n{}", this.serverCall.getMethodDescriptor().getFullMethodName(), message);
            }

            this.serverCall.sendMessage(message);
        }

        public void close(Status status, Metadata trailers) {
            this.serverCall.close(status, trailers);
        }

        public boolean isCancelled() {
            return this.serverCall.isCancelled();
        }

        public MethodDescriptor<M, R> getMethodDescriptor() {
            return this.serverCall.getMethodDescriptor();
        }
    }
}

