package com.springlearning.rest.webservices.restfulwebservices.sqsComponents;

import com.springlearning.rest.webservices.restfulwebservices.common.DefaultMessageSerializer;
import com.springlearning.rest.webservices.restfulwebservices.common.MessageSerializer;
import com.springlearning.rest.webservices.restfulwebservices.infra.InfraSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
public final class SqsQueue<T> implements Queue<T> {
    private static final int DEFAULT_MAX_NUMBER_OF_MESSAGES = 10;

    private InfraSetup infra;

    private SqsClient sqsClient;

    @Value("${sqs.createIfAbsent}")
    private boolean shouldCreateQueue; // this is having sideEffects

    private String queueName;

    private String queueUrl;

    private MessageSerializer<T> messageSerializer;

    public SqsQueue() {
    }

    public SqsQueue(@Autowired InfraSetup infra, @Autowired SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        this.infra = infra;
    }

    public SqsQueue(MessageSerializer<T> messageSerializer, String queueName, InfraSetup infra, SqsClient sqsClient) {
        this.messageSerializer = messageSerializer;
        this.queueName = queueName;
        this.infra = infra;
        this.sqsClient = sqsClient;
        this.shouldCreateQueue = true; // @TODO hardCoded
        setQueueUrl();
    }

    public SqsQueue(Type type, String queueName, InfraSetup infra, SqsClient sqsClient) {
        this(new DefaultMessageSerializer<T>(type), queueName, infra, sqsClient);
        shouldCreateQueueAtInitialization();
    }

    private void shouldCreateQueueAtInitialization() {
        // if create on initialization is true then create it
        if (shouldCreateQueue) {
            // if queue is not present;
            if (!isPresent()) {
                createQueue();
            }
        }
    }

    private void createQueue() {
        infra.createQueue(queueName);
    }

    //    @TODO hardCoded
    private void setQueueUrl() {
        // get the host
        // get the port
        // create queue url
        this.queueUrl = String.format("http://%s:%s/queue/%s", "localhost", "4576", queueName);
    }

    private boolean isPresent() {
        try {
            GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest
                    .builder()
                    .queueName(queueName)
                    .build();
            sqsClient.getQueueUrl(getQueueUrlRequest);
            return true;
        } catch (QueueDoesNotExistException existException) {
            return false;
        }
    }

    @Override
    public boolean enqueue(T element) throws Exception {
        return enqueue(element, 0);
    }

    @Override
    public boolean enqueue(T element, Integer delaySeconds) throws Exception {
        // convert the message using message Serializer
        // create the sendMessageRequest
        // send the message
        try {
            String serializedMessage = messageSerializer.serialize(element);
            SendMessageRequest sendMessageRequest = SendMessageRequest
                    .builder()
                    .queueUrl(queueUrl)
                    .messageBody(serializedMessage)
                    .delaySeconds(delaySeconds)
                    .build();
            sqsClient.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return true;
    }

    @Override
    public T dequeue() {
        List<T> messages = dequeue(1);
        if (!messages.isEmpty()) {
            return messages.get(0);
        }
        return null;
    }

    @Override
    public List<T> dequeue(int maxNumberOfElements) {
        List<T> messages = new ArrayList<>();
        List<ConsumedElement<T>> peekElements = peek(maxNumberOfElements);
        for (ConsumedElement<T> message : peekElements) {
            message.ack();
            messages.add(message.getElement());
        }
        return messages;
    }

    @Override
    public ConsumedElement<T> peek() {
        List<ConsumedElement<T>> elements = peek(1);
        if (elements.size() > 0) return elements.get(0);
        return null;
    }

    @Override
    public List<ConsumedElement<T>> peek(int numberOfElements) {
        return peek(numberOfElements, null);
    }

    public List<ConsumedElement<T>> peek(int maxNumberOfMessages, Integer waitTimeSeconds) {
        List<ConsumedElement<T>> messages = new ArrayList<>();
        try {
            if (maxNumberOfMessages < 0) maxNumberOfMessages = 1;
            else if (maxNumberOfMessages > DEFAULT_MAX_NUMBER_OF_MESSAGES)
                maxNumberOfMessages = DEFAULT_MAX_NUMBER_OF_MESSAGES;

            //create the receive Message Request
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest
                    .builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(maxNumberOfMessages)
                    .attributeNames(QueueAttributeName.ALL)
                    .waitTimeSeconds(waitTimeSeconds != null && waitTimeSeconds > 0 ? waitTimeSeconds : 0)
                    .build();
            ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
            if (receiveMessageResponse != null) {
                List<Message> messageList = receiveMessageResponse.messages();
                for (Message message : messageList) {
                    T deserializedMessage = messageSerializer.deserialize(message.body());
                    SqsConsumedMessage<T> consumedMessage = new SqsConsumedMessage<T>(message, deserializedMessage);
                    messages.add(consumedMessage);
                }
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return messages;
    }

    class SqsConsumedMessage<T> implements ConsumedElement<T> {
        private final Message sqsMessage;
        private final T element;

        public SqsConsumedMessage(Message message, T deserializedMessage) {
            this.element = deserializedMessage;
            this.sqsMessage = message;
        }

        @Override
        public T getElement() {
            return element;
        }

        @Override
        public void ack() {
            try {
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest
                        .builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(sqsMessage.receiptHandle())
                        .build();
                sqsClient.deleteMessage(deleteMessageRequest);
            } catch (Exception ignored) {

            }
        }

        @Override
        public void suspend(long suspendDurationMsec) {
            try {
                ChangeMessageVisibilityRequest messageVisibilityRequest = ChangeMessageVisibilityRequest
                        .builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(sqsMessage.receiptHandle())
                        .visibilityTimeout((int) suspendDurationMsec / 1000)
                        .build();
                sqsClient.changeMessageVisibility(messageVisibilityRequest);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void release() {
            suspend(0);
        }

        @Override
        public String toString() {
            return "SqsConsumedMessage{" +
                    "element=" + element +
                    '}';
        }
    }
}
