package com.springlearning.rest.webservices.restfulwebservices.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class BaseJacksonSerialization<T> {
    private final ObjectReader _objectReader;
    private final ObjectWriter _objectWriter;

    public BaseJacksonSerialization(Type type) {
        this(new ObjectMapper(), type);
    }

    private BaseJacksonSerialization(ObjectMapper objectMapper, Type type) {
        _objectReader = objectMapper.readerFor(objectMapper.constructType(type));
        _objectWriter = objectMapper.writerFor(objectMapper.constructType(type));
    }

    public String serialize(T object) throws IOException {
        return object == null ? null : _objectWriter.writeValueAsString(object);
    }

    public T deserialize(String jsonString) throws IOException {
        return jsonString == null ? null : _objectReader.readValue(jsonString);
    }
}
