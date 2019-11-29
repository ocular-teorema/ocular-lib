package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordingRequestDto {

    @JsonProperty("camera_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cameraId;

    @JsonProperty("recording")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRecording;

    @JsonProperty("request_uid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer requestUid;
}
