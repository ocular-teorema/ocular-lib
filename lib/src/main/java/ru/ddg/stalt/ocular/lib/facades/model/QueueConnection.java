package ru.ddg.stalt.ocular.lib.facades.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueConnection implements ru.ddg.stalt.ocular.lib.facades.model.Connection {

    private Connection connection;

    private Channel channel;
}
