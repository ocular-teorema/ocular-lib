package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * идентификатор, имя, список камер, список хранилищ, список расписаний
 */
@Getter
public class Server {

    private int id;

    private String name;

    private List<Camera> cameras;

    private List<Storage> storages;

    private List<Schedule> schedules;
}
