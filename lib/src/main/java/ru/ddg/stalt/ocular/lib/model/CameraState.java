package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CameraState {
    private String cameraId;

    private String status;

    private Integer errorCode;

    private String errorMessage;
}
