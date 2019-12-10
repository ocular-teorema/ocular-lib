package ru.ddg.stalt.ocular.lib.impl.services;

import com.rabbitmq.client.*;
import com.sun.tools.corba.se.idl.constExpr.Or;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.exceptions.DuplicateDriverIdException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.Driver;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ResponseService {
    private final static String RESPONSE_EXCHANGE = "driver";

    @Autowired
    private RequestRegistry requestRegistry;

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

        }

        @Override
        public void handleRecoverOk(String consumerTag) {

        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        }
    };

    @AllArgsConstructor
    private static class Subscription {
        Channel channel;
        String driverId;
        String queueName;
    }
}
