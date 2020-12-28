package com.springlearning.rest.webservices.restfulwebservices.common;

public interface MessageSerializer<T> {

    String serialize(T object) throws Exception;

    T deserialize(String message) throws Exception;
}
