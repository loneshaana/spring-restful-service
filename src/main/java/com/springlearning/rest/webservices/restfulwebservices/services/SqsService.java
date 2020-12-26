package com.springlearning.rest.webservices.restfulwebservices.services;

import com.springlearning.rest.webservices.restfulwebservices.producer.SqsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqsService {

    @Autowired
    private SqsProducer sqsProducer;

    @PostMapping(path = "/send/push-to-sqs")
    void pushToSqs(@RequestBody Object data){
        /*
            This is our producer which pushes the data to the sqs
         */
        sqsProducer.pushToSqs(data);
    }
}
