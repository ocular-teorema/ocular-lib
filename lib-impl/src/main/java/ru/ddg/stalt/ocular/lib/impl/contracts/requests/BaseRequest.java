package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

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
        @JsonSubTypes.Type(value = ServerStatusRequest.class, name = "status_request"),
        @JsonSubTypes.Type(value = ArchiveRecordRequest.class, name = "archive_video_request"),
        @JsonSubTypes.Type(value = CameraListRequest.class, name = "camera_list_request"),
        @JsonSubTypes.Type(value = StorageRequest.class, name = "storage_list"),
        @JsonSubTypes.Type(value = ConfigImportRequest.class, name = "config_import_request")
})
public class BaseRequest {
    @JsonProperty("uuid")
    private UUID uuid;
    private String server;
}
