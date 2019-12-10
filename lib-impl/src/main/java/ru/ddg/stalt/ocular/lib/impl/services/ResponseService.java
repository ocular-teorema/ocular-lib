package ru.ddg.stalt.ocular.lib.impl.services;

import com.rabbitmq.client.*;
import com.sun.tools.corba.se.idl.constExpr.Or;
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
        if (subscriptions.contains(driverId)) {
            throw new DuplicateDriverIdException(driverId);
        }
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(RESPONSE_EXCHANGE, BuiltinExchangeType.DIRECT);
        String queueName = "driver.v1." + driverId;
        channel.queueDeclare(queueName, true, false, true, null);
        channel.exchangeBind(RESPONSE_EXCHANGE, queueName, driverId);
        channel.basicConsume(queueName, true, driverId, consumer);
    }

    public void unsubscribe(@NonNull String driverId) throws IOException, TimeoutException {
        final Subscription subscription = subscriptions.remove(driverId);
        if (subscription == null) {
            return;
        }
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

    private static class Subscription {
        Channel channel;
        String driverId;
    }
}
