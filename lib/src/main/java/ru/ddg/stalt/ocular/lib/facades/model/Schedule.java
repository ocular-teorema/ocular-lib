package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;
import lombok.Setter;

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
}
