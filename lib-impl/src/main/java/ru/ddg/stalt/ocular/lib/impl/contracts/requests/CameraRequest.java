package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;

import java.util.UUID;

@Getter
@Setter
public class CameraRequest extends BaseRequest {

    @JsonProperty("camera_id")
    private String cameraId;

    private CameraDto cameraDto;

    public CameraRequest(UUID uuid, String server, String type) {
        super(uuid, server, type);
    }
}
