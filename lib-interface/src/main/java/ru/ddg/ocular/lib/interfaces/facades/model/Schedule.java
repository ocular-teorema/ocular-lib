package ru.ddg.ocular.lib.interfaces.facades.model;

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
