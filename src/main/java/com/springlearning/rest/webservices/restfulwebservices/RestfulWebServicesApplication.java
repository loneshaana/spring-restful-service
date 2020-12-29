package com.springlearning.rest.webservices.restfulwebservices;

import com.springlearning.rest.webservices.restfulwebservices.configs.AwsCredentialConfig;
import com.springlearning.rest.webservices.restfulwebservices.configs.AwsSqsConfig;
import com.springlearning.rest.webservices.restfulwebservices.configs.DynamoProperties;
import com.springlearning.rest.webservices.restfulwebservices.infra.Infra;
import com.springlearning.rest.webservices.restfulwebservices.infra.InfraSetup;
import com.springlearning.rest.webservices.restfulwebservices.sqsComponents.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@SpringBootApplication
@EnableConfigurationProperties({DynamoProperties.class, AwsCredentialConfig.class, AwsSqsConfig.class})
public class RestfulWebServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulWebServicesApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    // why the method name can't be changed
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message");
        return messageSource;
    }

    // _________________________________________________AWS________________________________________
    @Bean
    AwsCredentialsProvider awsCredentialsProvider(AwsCredentialConfig credentialConfig) {
        return new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return AwsBasicCredentials.create(credentialConfig.getAccessKey(), credentialConfig.getSessionKey());
            }
        };
    }

    // create the dynamoDb client
    @Bean
    DynamoDbClient dynamoDbClient(AwsCredentialsProvider awsCredentialsProvider, DynamoProperties dynamoProperties) {
        return DynamoDbClient
                .builder()
                .credentialsProvider(awsCredentialsProvider)
                .endpointOverride(dynamoProperties.getEndpoint())
                .build();
    }

    @Bean
    S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
        S3ClientBuilder builder = S3Client.builder();
        return builder.credentialsProvider(awsCredentialsProvider).build();
    }

    @Bean
    SqsClient sqsClient(AwsCredentialsProvider awsCredentialsProvider, AwsSqsConfig awsSqsConfig) {
        SqsClientBuilder builder = SqsClient.builder();
        return builder.credentialsProvider(awsCredentialsProvider)
                .endpointOverride(awsSqsConfig.getEndpoint())
                .build();
    }

    @Bean
    InfraSetup infraSetup(SqsClient sqsClient, DynamoDbClient dynamoDbClient) {
        return new Infra(sqsClient, dynamoDbClient);
    }

    @Bean
    Queue<Object> sqsObjectBean(SqsClient sqsClient, InfraSetup infraSetup) {
        return new SqsQueue<>(Object.class, "testing", infraSetup, sqsClient);
    }

    @Bean
    MessageConsumer<Object> messageConsumerBean() {
        return new MessageConsumerService<Object>();
    }

    @Bean
    SqsConsumerService<Object> sqsConsumerServiceLifeCycle(Queue<Object> sqsObjectBean, MessageConsumer<Object> messageConsumerBean) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5);
        return new SqsConsumerService<Object>(scheduledExecutorService, sqsObjectBean, messageConsumerBean);
    }

    @Bean
    ApplicationRunner applicationRunner(SqsConsumerService<Object> sqsConsumerServiceLifeCycle) {
        return args -> {
            System.out.println("Invoking Sqs polling");
            sqsConsumerServiceLifeCycle.onStart();
        };
    }
}
