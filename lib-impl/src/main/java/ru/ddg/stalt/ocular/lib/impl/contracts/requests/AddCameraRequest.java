package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;

import java.util.UUID;

@Getter
@Setter
public class AddCameraRequest extends BaseRequest {

    private CameraDto data;

    public AddCameraRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
