package com.springlearning.rest.webservices.restfulwebservices.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.HashMap;
import java.util.Map;

/*
    This class is responsible
    for creating aws components
 */
@Controller
public class Infra {
    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private SqsClient sqsClient;

    // create dynamo components
    void createDynamoTable(String tableName, String uniqueAttributeName) {
        CreateTableRequest createTableRequest = CreateTableRequest
                .builder()
                .tableName(tableName)
                .keySchema(KeySchemaElement
                        .builder()
                        .keyType(KeyType.HASH)
                        .attributeName(uniqueAttributeName)
                        .build())
                .attributeDefinitions(AttributeDefinition
                        .builder()
                        .attributeName(uniqueAttributeName)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .provisionedThroughput(ProvisionedThroughput
                        .builder()
                        .writeCapacityUnits(5L)
                        .readCapacityUnits(5L)
                        .build())
                .build();
        dynamoDbClient.createTable(createTableRequest);
    }

    // create sqs components
    void createQueue(String queueName) {
        Map<QueueAttributeName, String> attributes = new HashMap<>();
        attributes.put(QueueAttributeName.DELAY_SECONDS, "5");
        CreateQueueRequest queueRequest = CreateQueueRequest.builder().queueName(queueName).attributes(attributes).build();
        sqsClient.createQueue(queueRequest);
    }

    // create s3 components

}
