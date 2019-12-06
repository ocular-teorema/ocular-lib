package ru.ddg.stalt.ocular.lib.impl.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.impl.contracts.BaseResponse;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.BaseRequest;
import ru.ddg.stalt.ocular.lib.impl.model.OcularConnection;

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

    public <T extends BaseResponse> T send(OcularConnection ocularConnection, BaseRequest request, Class<T> responseClass) throws IOException, TimeoutException {
        byte[] json = objectMapper.writeValueAsBytes(request);

        try (Channel channel = ocularConnection.getConnection().createChannel()) {
            channel.basicPublish(request.getServer(), "", null, json);
        }
        return null;
    }
}
