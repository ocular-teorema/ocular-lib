package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.UUID;

@Setter
public class DeleteCameraRrequest extends BaseRequest {
    @JsonProperty("camera_id")
    private String cameraId;

    public DeleteCameraRrequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
