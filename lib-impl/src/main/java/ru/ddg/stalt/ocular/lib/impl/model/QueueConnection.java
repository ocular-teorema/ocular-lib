package ru.ddg.stalt.ocular.lib.impl.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueConnection implements ru.ddg.stalt.ocular.lib.model.Connection {

    private Connection connection;

    private Channel channel;
}
