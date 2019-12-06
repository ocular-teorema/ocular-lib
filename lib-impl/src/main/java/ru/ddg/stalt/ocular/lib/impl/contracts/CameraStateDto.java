package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CameraStateDto {
    private String cameraId;

    private String status;

    private Integer errorCode;

    @JsonProperty("error_message")
    private String errorMessage;
}
