package ru.ddg.stalt.ocular.lib.impl.contracts.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.OrganizationListDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.ServerStateDto;

import java.util.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerStateDto.class, name = "status"),
        @JsonSubTypes.Type(value = RecordListResponse.class, name = "archive_video"),
        @JsonSubTypes.Type(value = CameraListResponse.class, name = "cameras_list_response"),
        @JsonSubTypes.Type(value = StorageListResponse.class, name = "storage_list"),
        @JsonSubTypes.Type(value = ScheduleListResponse.class, name = "schedules_list_response"),
        @JsonSubTypes.Type(value = OrganizationListDto.class, name = "config_export"),
        @JsonSubTypes.Type(value = RecordResponse.class, name = "archive_video"),
        @JsonSubTypes.Type(value = AddCameraResponse.class, name = "cameras_add"),
        @JsonSubTypes.Type(value = CameraRecordingResponse.class, name = "cameras_set_recording"),
        @JsonSubTypes.Type(value = ErrorResponse.class, name = "error")
})
public class BaseResponse<T> {
    @JsonProperty("uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID requestUuid;
    private T data;
}
