package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * идентификатор, имя, список камер, список хранилищ, список расписаний
 */
@Getter
@Setter
public class Server {

    private String id;

    private String name;

    private List<Camera> cameras;

    private List<Storage> storages;

    private List<Schedule> schedules;
}
