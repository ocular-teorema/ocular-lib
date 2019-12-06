package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Данный объект содержит информацию о расписании:
 * идентификатор расписания, используемого камерой
 * тип расписания
 */
@Getter
@Setter
public class Schedule {

    private int id;

    private String type;

    private List<Integer> weekDays;

    private Integer startTimestamp;

    private Integer stopTimestamp;

    private String startTime;

    private String stopTime;
}
