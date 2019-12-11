package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.BaseResponse;

@Getter
@Setter
@AllArgsConstructor
public class PtzControlDto extends BaseResponse {

    @JsonProperty("camera_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cameraId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer step;
}
