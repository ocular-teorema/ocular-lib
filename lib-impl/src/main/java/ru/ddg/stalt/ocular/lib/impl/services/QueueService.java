package ru.ddg.stalt.ocular.lib.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.impl.contracts.Response;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.BaseRequest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class QueueService {
    private final static String VHOST = "/ocular";

    @Autowired
    private ObjectMapper objectMapper;

    public Connection createConnection(String host, int port, String username, String password) throws IOException, TimeoutException {
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(VHOST);
        return connectionFactory.newConnection();

    }

    public Response send(Connection connection, BaseRequest request) throws IOException, TimeoutException {
        byte[] json = objectMapper.writeValueAsBytes(request);

        try (Channel channel = connection.createChannel()) {
            channel.basicPublish("", request.getServer(), null, json);
        }
        return null;
    }
}
