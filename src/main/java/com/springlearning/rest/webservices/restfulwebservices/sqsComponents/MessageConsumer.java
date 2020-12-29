package com.springlearning.rest.webservices.restfulwebservices.sqsComponents;

public interface MessageConsumer<T> {
    void onMessageConsumed(ConsumedElement<T> element);
}