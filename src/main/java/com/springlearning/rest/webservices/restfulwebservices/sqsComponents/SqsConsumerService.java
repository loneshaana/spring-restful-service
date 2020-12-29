package com.springlearning.rest.webservices.restfulwebservices.sqsComponents;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public
class SqsConsumerService<T> implements LifeCycle {
    private static final long DEFAULT_POLL_PERIOD_MSEC = 5000L;
    private static final int DEFAULT_MAX_NUMBER_OF_MESSAGES = 10;
    private static final int DEFAULT_LONG_POLLING_SECONDS = 20;

    protected long pollPeriodMsec;
    protected int maxNumberOfMessage;
    protected ScheduledExecutorService scheduledExecutorService;
    protected Queue<T> queue;

    protected MessageConsumer<T> messageConsumer;

    protected SqsConsumerService<T>.MessagePollingRunnable messagePollingRunnable;

    public SqsConsumerService() {
    }

    public SqsConsumerService(ScheduledExecutorService scheduledExecutorService, Queue<T> sqs, MessageConsumer<T> messageConsumer, long pollPeriodMsec, int maxNumberOfMessage) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.queue = sqs;
        this.messageConsumer = messageConsumer;
        if (pollPeriodMsec <= 0L)
            this.pollPeriodMsec = DEFAULT_POLL_PERIOD_MSEC;
        else
            this.pollPeriodMsec = pollPeriodMsec;

        this.maxNumberOfMessage = maxNumberOfMessage > 0 && maxNumberOfMessage <= 10 ? maxNumberOfMessage : DEFAULT_MAX_NUMBER_OF_MESSAGES;
        this.messagePollingRunnable = new SqsConsumerService<T>.MessagePollingRunnable();
        //@TODO fix below
//        LifeCycleManager.registerLifeCycle(this);
    }

    public SqsConsumerService(ScheduledExecutorService scheduledExecutorService, Queue<T> queue, MessageConsumer<T> messageConsumer) {
        this(scheduledExecutorService, queue, messageConsumer, DEFAULT_POLL_PERIOD_MSEC, DEFAULT_MAX_NUMBER_OF_MESSAGES);
    }

    @Override
    public void onStart() throws Exception {
        System.out.println("Sqs Consumer Started!");
        this.scheduledExecutorService.submit(this.messagePollingRunnable);
    }

    @Override
    public void onShutdown() throws Exception {

    }

    protected void dispatchConsumedMessage(final ConsumedElement<T> consumedMessage) {
        this.scheduledExecutorService.submit(() -> {
            synchronized (this) {
                if (this.messageConsumer != null) {
                    this.messageConsumer.onMessageConsumed(consumedMessage);
                }
            }
        });
    }

    protected class MessagePollingRunnable implements Runnable {
        @Override
        public void run() {
            if (SqsConsumerService.this.messageConsumer != null) {
                List<ConsumedElement<T>> consumedElements = SqsConsumerService.this.queue.peek(SqsConsumerService.this.maxNumberOfMessage, SqsConsumerService.DEFAULT_LONG_POLLING_SECONDS);
                for (ConsumedElement<T> consumedElement : consumedElements)
                    SqsConsumerService.this.dispatchConsumedMessage(consumedElement);
            }
            SqsConsumerService.this.scheduledExecutorService.schedule(this, SqsConsumerService.this.pollPeriodMsec, TimeUnit.MILLISECONDS);
        }
    }
}
