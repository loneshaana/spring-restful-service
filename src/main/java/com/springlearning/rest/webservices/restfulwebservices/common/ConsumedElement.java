package com.springlearning.rest.webservices.restfulwebservices.common;

public interface ConsumedElement<T> {
    T getElement();

    void ack();

    void suspend(long suspendDurationMsec);

    void release();

}
