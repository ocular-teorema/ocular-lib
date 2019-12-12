package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleDto {
    private int id;

    @JsonProperty("schedule_type")
    private String type;

    private List<Integer> weekDays;

    @JsonProperty("start_timestamp")
    private Integer startTimestamp;

    @JsonProperty("stop_timestamp")
    private Integer stopTimestamp;

    private String startTime;

    private String stopTime;
}
