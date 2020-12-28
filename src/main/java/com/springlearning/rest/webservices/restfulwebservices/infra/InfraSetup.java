package com.springlearning.rest.webservices.restfulwebservices.infra;

public interface InfraSetup {
    void createDynamoTable(String tableName, String uniqueAttributeName);

    void createQueue(String queueName);
}
