package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.ScheduleDto;

import java.util.UUID;

@Getter
@Setter
public class ScheduleRequest extends BaseRequest {

    @JsonProperty("schedule_id")
    private Integer scheduleId;

    private ScheduleDto scheduleDto;

    public ScheduleRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
