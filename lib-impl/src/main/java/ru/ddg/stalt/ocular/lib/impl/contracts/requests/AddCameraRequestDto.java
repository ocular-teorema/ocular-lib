package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;

import java.util.UUID;

@Getter
@Setter
public class AddCameraRequestDto implements Request {
    @JsonProperty("request_uid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID requestUid;

    private CameraDto camera;

}
