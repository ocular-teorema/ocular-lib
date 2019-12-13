package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import java.util.UUID;

public class ExportConfigRequest extends BaseRequest {
    public ExportConfigRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
