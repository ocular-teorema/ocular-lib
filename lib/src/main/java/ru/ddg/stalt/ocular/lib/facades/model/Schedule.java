package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

/**
 * Данный объект содержит информацию о расписании:
 * идентификатор расписания, используемого камерой
 * тип расписания
 */
@Getter
public class Schedule {

    int id;

    String type;
}
