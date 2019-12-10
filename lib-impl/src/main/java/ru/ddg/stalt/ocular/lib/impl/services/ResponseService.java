package ru.ddg.stalt.ocular.lib.impl.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.exceptions.DuplicateDriverIdException;
import ru.ddg.stalt.ocular.lib.impl.contracts.BaseResponse;
import ru.ddg.stalt.ocular.lib.impl.exceptions.RequestNotFoundException;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ResponseService {
    private final static String RESPONSE_EXCHANGE = "driver";

    @Autowired
    private RequestRegistry requestRegistry;

    @Autowired
    private ObjectMapper objectMapper;

    private final ConcurrentHashMap<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    public void subscribe(@NonNull String driverId, @NonNull Connection connection) throws IOException, TimeoutException, DuplicateDriverIdException {
        final Channel channel = connection.createChannel();
        final String queueName = "driver.v1." + driverId;
        final Subscription exists = subscriptions.putIfAbsent(driverId, new Subscription(channel, driverId, queueName));
        if (exists != null) {
            channel.close();
            throw new DuplicateDriverIdException(driverId);
        }
        channel.exchangeDeclare(RESPONSE_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(queueName, true, false, true, null);
        channel.queueBind(queueName, RESPONSE_EXCHANGE, null);
        channel.basicConsume(queueName, true, driverId, consumer);
        log.info("Subscribe to queue {}.", queueName);
        channel.basicRecover();
        log.info("Recover messages.");
    }

    public void unsubscribe(@NonNull String driverId) throws IOException, TimeoutException {
        final Subscription subscription = subscriptions.remove(driverId);
        if (subscription == null) {
            return;
        }
        subscription.channel.basicCancel(subscription.driverId);
        subscription.channel.close();
    }

    private void unsubscribeSilent(String driverId) {
        try {
            unsubscribe(driverId);
        }
        catch (Exception ex) {
            log.warn("Silent unsubscribe {} error occurs. Ignored.", driverId);
        }
    }

    @PreDestroy
    public void close() {
        subscriptions.keySet().forEach(this::unsubscribeSilent);
    }

    private final Consumer consumer = new Consumer() {
        @Override
        public void handleConsumeOk(String consumerTag) {

        }

        @Override
        public void handleCancelOk(String consumerTag) {
            try {
                unsubscribe(consumerTag);
            }
            catch (IOException | TimeoutException e) {
                log.warn("Consumer canceled by queue.", e);
            }
        }

        @Override
        public void handleCancel(String consumerTag) throws IOException {

        }

        @Override
        public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
            try {
                unsubscribe(consumerTag);
            }
            catch (IOException | TimeoutException e) {
                log.warn("Consumer canceled by shutdown signal.", e);
            }
        }

        @Override
        public void handleRecoverOk(String consumerTag) {

        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            if (!subscriptions.contains(consumerTag)) {
                log.warn("Message received for unknown tag {}. Skip it.", consumerTag);
                return;
            }
            BaseResponse response;
            try {
                response = objectMapper.readValue(body, BaseResponse.class);
            }
            catch (Exception e) {
                log.warn("Impossible to parse message:\n{}.", new String(body, StandardCharsets.US_ASCII), e);
                return;
            }
            try {
                requestRegistry.response(response.getRequestUuid(), response);
            }
            catch (RequestNotFoundException e) {
                log.warn("There was no request {}, but response are received:\n{}", response.getRequestUuid(), new String(body, StandardCharsets.US_ASCII), e);
            }
        }
    };

    @AllArgsConstructor
    private static class Subscription {
        Channel channel;
        String driverId;
        String queueName;
    }
}
