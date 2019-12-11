package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import java.util.UUID;

public class StorageListRequest extends BaseRequest {
    public StorageListRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
