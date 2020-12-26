package com.springlearning.rest.webservices.restfulwebservices.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import software.amazon.awssdk.services.sqs.SqsClient;

@Controller
public class SqsProducer {

    @Autowired
    private SqsClient sqsClient;

    /*
        This api will be used to push the data to the sqs
     */
    public void pushToSqs(Object data) {
    }
}
