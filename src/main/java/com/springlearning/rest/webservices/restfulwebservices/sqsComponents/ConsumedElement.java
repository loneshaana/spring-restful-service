package com.springlearning.rest.webservices.restfulwebservices.sqsComponents;

public interface ConsumedElement<T> {
    public T getElement();

    public void ack();

    public void suspend(long suspendDurationMsec);

    public void release();

}
