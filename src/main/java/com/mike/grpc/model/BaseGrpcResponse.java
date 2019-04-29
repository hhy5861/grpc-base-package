package com.mike.grpc.model;

import com.mike.grpc.proto.GrpcResponse;

import java.io.Serializable;

public class BaseGrpcResponse<T> implements Serializable {


    public static final int DEFAULT_CODE = 0;

    public static final int DEFAULT_ERROR_CODE = -100000;

    public static final String DEFAULT_MESSAGE = "ok";

    private int code;

    private String message;

    private T data;

    private String dataString;

    private IGrpcSerialize<T> grpcSerialize;

    BaseGrpcResponse() {
    }

    public GrpcResponse response() {
        if (this.data instanceof String) {
            this.dataString = String.valueOf(this.data);
        } else if (this.data != null) {
            this.dataString = this.grpcSerialize.Serialize(this.data);
        }

        GrpcResponse.Builder responseBuilder = GrpcResponse.newBuilder()
                .setCode(this.code)
                .setMessage(this.message)
                .setData(this.dataString);

        return responseBuilder.build();
    }

    public GrpcResponse paramsException(Exception e) {
        return paramsException(DEFAULT_ERROR_CODE, e);
    }

    public GrpcResponse paramsException(int errCode, Exception e) {
        return paramsException(errCode, e.getMessage());
    }

    public GrpcResponse paramsException(int errCode, String errMsg) {
        GrpcResponse.Builder responseBuilder = GrpcResponse.newBuilder()
                .setCode(errCode)
                .setMessage(errMsg);

        return responseBuilder.build();
    }

    public T getData() throws Exception {
        if (this.dataString.isEmpty()) {
            throw new Exception("data is empty");
        }

        return this.grpcSerialize.Deserialize(this.dataString);
    }

    public boolean isSuccess() {
        return this.code >= DEFAULT_CODE;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static <T> Builder newBuilder(T data) {
        return new Builder<>(data);
    }

    public static final class Builder<T> {

        private int code = DEFAULT_CODE;

        private String message = DEFAULT_MESSAGE;

        private T data;

        private IGrpcSerialize<T> grpcSerialize;

        private Builder() {
        }

        private Builder(T data) {
            this.data = data;
        }

        public BaseGrpcResponse<T> build() {
            BaseGrpcResponse<T> baseGrpcResponse = new BaseGrpcResponse<>();

            baseGrpcResponse.data = this.data;
            baseGrpcResponse.message = this.message;
            baseGrpcResponse.code = this.code;

            if (baseGrpcResponse.grpcSerialize == null) {
                setGrpcSerialize(new FastJsonSerialize<>());
            } else {
                baseGrpcResponse.grpcSerialize = getGrpcSerialize();
            }

            return baseGrpcResponse;
        }

        public T getData() {
            return data;
        }

        public Builder setData(T data) {
            this.data = data;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public int getCode() {
            return code;
        }

        public Builder setCode(int code) {
            this.code = code;
            return this;
        }

        public Builder setGrpcSerialize(IGrpcSerialize<T> grpcSerialize) {
            this.grpcSerialize = grpcSerialize;

            return this;
        }

        public IGrpcSerialize<T> getGrpcSerialize() {
            return grpcSerialize;
        }
    }
}
