package com.springlearning.rest.webservices.restfulwebservices.sqsComponents;

import org.springframework.stereotype.Component;

@Component
public class MessageConsumerService<T> implements MessageConsumer<T> {
    @Override
    public void onMessageConsumed(ConsumedElement<T> element) {
        System.out.println("Received Message");
        element.ack(); // removes the element from queue
        System.out.println(element.toString());
    }
}