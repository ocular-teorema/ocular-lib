package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RecordingRequest extends BaseRequest {

    @JsonProperty("camera_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cameraId;

    @JsonProperty("recording")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRecording;

    public RecordingRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
