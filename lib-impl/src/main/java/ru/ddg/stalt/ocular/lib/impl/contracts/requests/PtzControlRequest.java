package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.PtzControlDto;

import java.util.UUID;

@Getter
@Setter
public class PtzControlRequest extends BaseRequest {
    private PtzControlDto ptzControl;

    public PtzControlRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
