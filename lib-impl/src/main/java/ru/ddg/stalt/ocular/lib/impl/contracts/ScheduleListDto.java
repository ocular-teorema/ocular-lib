package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleListDto {

    @JsonProperty("weekdays")
    private List<ScheduleDto> weekDays;

    private List<ScheduleDto> timestamp;

    @JsonProperty("time_period")
    private List<ScheduleDto> timePeriod;
}
