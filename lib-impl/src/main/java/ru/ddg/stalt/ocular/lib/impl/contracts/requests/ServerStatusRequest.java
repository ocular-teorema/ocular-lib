package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import java.util.UUID;

public class ServerStatusRequest extends BaseRequest {
    public ServerStatusRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
