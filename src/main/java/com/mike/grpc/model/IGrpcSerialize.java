package com.mike.grpc.model;

public interface IGrpcSerialize<T> {
    String Serialize(T obj);

    T Deserialize(String s);
}
