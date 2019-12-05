package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ArchiveRecordRequest extends BaseRequest {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CameraDto> cameras;

    @JsonProperty("start_timestamp")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer startTimestamp;

    @JsonProperty("stop_timestamp")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer endTimestamp;

    private Integer skip;

    private Integer limit;

    public ArchiveRecordRequest(UUID uuid, String server, String type) {
        super(uuid, server, type);
    }
}
