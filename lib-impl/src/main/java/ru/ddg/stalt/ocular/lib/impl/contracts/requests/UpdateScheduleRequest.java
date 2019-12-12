package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.ScheduleDto;

import java.util.UUID;

@Getter
@Setter
public class UpdateScheduleRequest extends BaseRequest {

    @JsonProperty("schedule_id")
    private Integer scheduleId;

    @JsonProperty("data")
    private ScheduleDto scheduleDto;

    public UpdateScheduleRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
