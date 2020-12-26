package com.springlearning.rest.webservices.restfulwebservices.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("dynamo")
public class DynamoProperties {
    private URI endpoint;

    public DynamoProperties() {
    }

    public DynamoProperties(URI endpoint) {
        this.endpoint = endpoint;
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }
}
