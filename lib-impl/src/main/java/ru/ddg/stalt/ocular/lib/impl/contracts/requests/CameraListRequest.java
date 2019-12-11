package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import java.util.UUID;

public class CameraListRequest extends BaseRequest {
    public CameraListRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
