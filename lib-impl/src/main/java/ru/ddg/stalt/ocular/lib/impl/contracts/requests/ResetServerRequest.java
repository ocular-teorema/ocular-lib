package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import java.util.UUID;

public class ResetServerRequest extends BaseRequest {
    public ResetServerRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
