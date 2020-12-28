package com.springlearning.rest.webservices.restfulwebservices.common;

import org.springframework.stereotype.Controller;

import java.lang.reflect.Type;

public class DefaultMessageSerializer<T> implements MessageSerializer<T> {
    private final BaseJacksonSerialization<T> jacksonSerialization;

    public DefaultMessageSerializer(Type type) {
        jacksonSerialization = new BaseJacksonSerialization<>(type);
    }

    @Override
    public String serialize(T object) throws Exception {
        return jacksonSerialization.serialize(object);
    }

    @Override
    public T deserialize(String message) throws Exception {
        return jacksonSerialization.deserialize(message);
    }
}
