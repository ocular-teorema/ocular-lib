package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import java.util.UUID;

public class ScheduleListRequest extends BaseRequest {
    public ScheduleListRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
