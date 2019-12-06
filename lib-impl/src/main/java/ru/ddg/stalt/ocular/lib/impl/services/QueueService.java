package ru.ddg.stalt.ocular.lib.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.exceptions.ResponseTimeoutException;
import ru.ddg.stalt.ocular.lib.impl.contracts.BaseResponse;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.BaseRequest;
import ru.ddg.stalt.ocular.lib.impl.exceptions.DuplicateRequestException;
import ru.ddg.stalt.ocular.lib.impl.model.OcularConnection;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class QueueService {
    private final static String VHOST = "/ocular";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestRegistry requestRegistry;

    public Connection createConnection(String host, int port, String username, String password) throws IOException, TimeoutException {
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(VHOST);
        return connectionFactory.newConnection();

    }

    public <T extends BaseResponse> T send(OcularConnection ocularConnection, BaseRequest request, Class<T> responseClass) throws ResponseTimeoutException {
        try {
            return innerSend(ocularConnection, request, responseClass);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("impossible to convert request to json", e);
        }
        catch (ExecutionException | InterruptedException e) {
            throw new UnsupportedOperationException("error on waiting response", e);
        }
        catch (TimeoutException e) {
            throw new ResponseTimeoutException("waiting response is timed out.", e);
        }
    }

    public <T extends BaseResponse> T innerSend(OcularConnection ocularConnection, BaseRequest request, Class<T> responseClass) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        try {
            requestRegistry.register(request.getUuid(), completableFuture, responseClass);
        }
        catch (DuplicateRequestException e) {
            throw new IllegalArgumentException("request has contain unique UUID.", e);
        }

        byte[] json = objectMapper.writeValueAsBytes(request);

        try (Channel channel = ocularConnection.getConnection().createChannel()) {
            channel.basicPublish(request.getServer(), "", null, json);
        }
        return completableFuture.get(ocularConnection.getTimeout(), TimeUnit.MILLISECONDS);
    }
}
