package com.springlearning.rest.webservices.restfulwebservices.common;

import java.util.List;

public interface Queue<T> {
    boolean enqueue(T element) throws Exception;

    boolean enqueue(T element, Integer delaySeconds) throws Exception;

    T dequeue();

    List<T> dequeue(int maxNumberOfElements);

    ConsumedElement<T> peek();

    List<ConsumedElement<T>> peek(int numberOfElements);
}
