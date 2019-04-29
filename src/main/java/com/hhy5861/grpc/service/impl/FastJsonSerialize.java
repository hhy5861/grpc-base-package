package com.hhy5861.grpc.service.impl;

import com.alibaba.fastjson.JSON;
import com.hhy5861.grpc.service.IGrpcSerialize;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FastJsonSerialize<T> implements IGrpcSerialize<T> {
    @Override
    public String Serialize(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public T Deserialize(String s) {
        Type type = ((ParameterizedType) (new BaseGrpcResponse<T>() {}).getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        return JSON.parseObject(s, type);
    }
}
