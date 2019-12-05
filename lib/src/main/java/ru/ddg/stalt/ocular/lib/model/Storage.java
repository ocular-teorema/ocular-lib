package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Данный объект содержит информацию о хранилище:
 * идентификатор, имя, путь к архиву по-умоолчанию.
 */
@Getter
@Setter
public class Storage {

    private int id;

    private String name;

    private String defaultArchivePath;
}
