package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerStateDto.class, name = "status"),
        @JsonSubTypes.Type(value = RecordListDto.class, name = "archive_video"),
        @JsonSubTypes.Type(value = CameraListDto.class, name = "camera_list"),
        @JsonSubTypes.Type(value = StorageListDto.class, name = "storage_list"),
        @JsonSubTypes.Type(value = ScheduleListDto.class, name = "schedule_list"),
        @JsonSubTypes.Type(value = OrganizationListDto.class, name = "config_export"),
        @JsonSubTypes.Type(value = RecordDto.class, name = "archive_video")
})
public class BaseResponse {

    private String uuid;

    private boolean success;
    @JsonProperty("code")
    private Integer errorCode;
    @JsonProperty("error")
    private String errorDescription;
}
