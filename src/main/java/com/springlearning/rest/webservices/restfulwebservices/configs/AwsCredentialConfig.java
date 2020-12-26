package com.springlearning.rest.webservices.restfulwebservices.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws.config")
public class AwsCredentialConfig {
    private String accessKey;
    private String sessionKey;

    public AwsCredentialConfig() {
    }

    public AwsCredentialConfig(String accessKey, String sessionKey) {
        this.accessKey = accessKey;
        this.sessionKey = sessionKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
