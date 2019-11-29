package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PtzControlDto {

    @JsonProperty("camera_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cameraId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String step;
}
