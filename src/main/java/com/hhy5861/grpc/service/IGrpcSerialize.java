package com.hhy5861.grpc.service;

public interface IGrpcSerialize<T> {
    String Serialize(T obj);

    T Deserialize(String s);
}
