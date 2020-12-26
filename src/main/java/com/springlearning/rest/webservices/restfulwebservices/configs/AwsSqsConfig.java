package com.springlearning.rest.webservices.restfulwebservices.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("sqs")
public class AwsSqsConfig {
    private URI endpoint;

    public AwsSqsConfig() {
    }

    public AwsSqsConfig(URI endpoint) {
        this.endpoint = endpoint;
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }
}
