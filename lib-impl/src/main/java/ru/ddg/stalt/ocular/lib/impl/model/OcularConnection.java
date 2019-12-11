package ru.ddg.stalt.ocular.lib.impl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.model.Connection;
import ru.ddg.stalt.ocular.lib.model.EventHandler;

import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Getter
public class OcularConnection implements Connection {
    private final com.rabbitmq.client.Connection connection;
    private final long timeout;
    @Setter
    private final AtomicReference<EventHandler> eventHandler = new AtomicReference<>();

}
