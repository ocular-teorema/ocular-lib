package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * идентификатор, имя, список камер, список хранилищ, список расписаний
 */
@Getter
public class Server {

    int id;

    String name;

    List<Camera> cameras;

    List<Storage> storages;

    List<Schedule> schedules;
}
