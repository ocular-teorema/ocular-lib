package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.model.CameraStatusEnum;

@Getter
@Setter
public class CameraDto extends BaseResponse {

    @JsonProperty("id")
    private String cameraId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonProperty("address_primary")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String primaryAddress;

    @JsonProperty("address_secondary")
    private String secondaryAddress;

    @JsonProperty("stream_address")
    private String streamAddress;

    @JsonProperty("storage_id")
    private Integer storageId;

    @JsonProperty("schedule_id")
    private Integer scheduleId;

    @JsonProperty("analysis_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer analysisType;

    private CameraStatusEnum status;

    @JsonProperty("enabled")
    private Boolean isRecording;

    @JsonProperty("storage_days")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer storageDays;
}
