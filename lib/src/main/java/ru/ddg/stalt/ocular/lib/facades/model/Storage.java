package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

/**
 * Данный объект содержит информацию о хранилище:
 * идентификатор, имя, путь.
 */
@Getter
public class Storage {

    int id;

    String name;

    String path;
}
