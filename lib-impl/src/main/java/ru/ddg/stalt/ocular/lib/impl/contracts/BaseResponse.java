package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.events.Event;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Event.class, name = "event"),
        @JsonSubTypes.Type(value = ServerDto.class, name = "status"),
        @JsonSubTypes.Type(value = CameraListDto.class, name = "camera_list"),
        @JsonSubTypes.Type(value = OrganizationDto.class, name = "config_export"),
        @JsonSubTypes.Type(value = RecordDto.class, name = "archive_video")
})
public class BaseResponse {
    @JsonProperty
    private boolean success;
    @JsonProperty("code")
    private Integer errorCode;
    @JsonProperty("error")
    private String errorDescription;
}
