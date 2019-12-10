package ru.ddg.stalt.ocular.lib.impl.services;

import com.rabbitmq.client.*;
import com.sun.tools.corba.se.idl.constExpr.Or;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Driver;
import java.util.concurrent.TimeoutException;

@Component
public class ResponseService {
    private final static String RESPONSE_EXCHANGE = "driver";

    @Autowired
    private RequestRegistry requestRegistry;

    public void listenResponses(@NonNull String driverId, @NonNull Connection connection) throws IOException, TimeoutException {
        try (Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(RESPONSE_EXCHANGE, BuiltinExchangeType.DIRECT);
            String queueName = "driver.v1." + driverId;
            channel.queueDeclare(queueName, true, false, true, null);
            channel.exchangeBind(queueName, RESPONSE_EXCHANGE, driverId);
            channel.basicConsume(queueName, consumer);
        }
    }

    private Consumer consumer = new Consumer() {
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
}
