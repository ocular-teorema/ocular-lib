package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerStatusRequest.class, name = "status"),
        @JsonSubTypes.Type(value = ResetServerRequest.class, name = "reset_request"),
        @JsonSubTypes.Type(value = ArchiveRecordRequest.class, name = "archive_video"),
        @JsonSubTypes.Type(value = CameraListRequest.class, name = "cameras_list"),
        @JsonSubTypes.Type(value = StorageListRequest.class, name = "storages_list"),
        @JsonSubTypes.Type(value = ConfigImportRequest.class, name = "config_import"),
        @JsonSubTypes.Type(value = ScheduleListRequest.class, name = "schedules_list")
})
public class BaseRequest {
    @JsonProperty("uuid")
    private UUID uuid;
    @JsonIgnore
    private String server;
}
