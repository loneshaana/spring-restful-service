package com.springlearning.rest.webservices.restfulwebservices.controllers;

import com.springlearning.rest.webservices.restfulwebservices.sqsComponents.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SqsController {

    @Autowired
    Queue<Object> sqsObjectBean; // this is needs to be refactored

    public SqsController() {
    }

    @PostMapping(value = "/sqs/send/data")
    void sendDataToSqs(@RequestBody Object data) throws Exception {
        sqsObjectBean.enqueue(data);
    }

    @GetMapping(value = "/sqs/poll/data")
    Object pollDataFromSqs() {
        return sqsObjectBean.dequeue();
    }

}
