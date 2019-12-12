package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;

import java.util.UUID;

@Getter
@Setter
public class UpdateCameraRequest extends BaseRequest {

    @JsonProperty("camera_id")
    private String cameraId;

    @JsonProperty("data")
    private CameraDto camera;

    public UpdateCameraRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
