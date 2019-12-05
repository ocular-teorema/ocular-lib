package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleDto {
    private int id;

    private String type;

    private List<Integer> weekDays;

    private Integer startTimestamp;

    private Integer stopTimestamp;

    private String startTime;

    private String stopTime;
}
