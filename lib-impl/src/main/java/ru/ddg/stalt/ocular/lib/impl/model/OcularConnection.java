package ru.ddg.stalt.ocular.lib.impl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ddg.stalt.ocular.lib.model.Connection;

@AllArgsConstructor
@Getter
public class OcularConnection implements Connection {
    private final com.rabbitmq.client.Connection connection;
    private final long timeout;

}
