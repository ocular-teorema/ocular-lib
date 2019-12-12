package ru.ddg.stalt.ocular.lib.impl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.services.OcularServiceImpl;
import ru.ddg.stalt.ocular.lib.model.Connection;
import ru.ddg.stalt.ocular.lib.model.EventHandler;

import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Getter
public class OcularConnection implements Connection {
    private final String driverId;
    private final com.rabbitmq.client.Connection connection;
    private final long timeout;
    private final AtomicReference<EventHandler> eventHandler = new AtomicReference<>();
    private final OcularServiceImpl sourceService;

    @Override
    public void close() throws Exception {
        sourceService.disconnect(this);
    }
}
