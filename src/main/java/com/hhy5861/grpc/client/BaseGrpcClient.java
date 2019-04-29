package com.hhy5861.grpc.client;

import com.hhy5861.common.tools.Response;
import com.hhy5861.grpc.service.impl.BaseGrpcResponse;
import com.hhy5861.grpc.service.impl.FastJsonSerialize;
import com.hhy5861.grpc.service.IGrpcSerialize;
import com.hhy5861.grpc.proto.GrpcResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public abstract class BaseGrpcClient {
    public static final String DEFAULT_ADDR = "127.0.0.1";

    public static final int DEFAULT_HOST = 6000;

    protected ManagedChannel channel;

    public BaseGrpcClient() {
        this(DEFAULT_ADDR, DEFAULT_HOST);
    }

    public BaseGrpcClient(String addr) {
        this(addr, DEFAULT_HOST);
    }

    public BaseGrpcClient(int host) {
        this(DEFAULT_ADDR, host);
    }

    public BaseGrpcClient(String addr, int host) {
        this(ManagedChannelBuilder.forAddress(addr, host).usePlaintext().build());
    }

    public BaseGrpcClient(ManagedChannel channel) {
        this.channel = channel;
    }

    public void shutdown() throws InterruptedException {
        this.channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Response getResponse(GrpcResponse grpcResp) {
        return getResponse(grpcResp, new FastJsonSerialize());
    }

    public Response<?> getResponse(GrpcResponse grpcResponse, IGrpcSerialize serialize) {
        if (grpcResponse.getCode() == BaseGrpcResponse.DEFAULT_CODE) {
            return new Response<>(serialize.Deserialize(grpcResponse.getData())
            );
        } else {
            return new Response<>(grpcResponse.getMessage()
            );
        }
    }
}
