package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

/**
 * Данный объект содержит информацию о записи камеры:
 * идентификатор записи, идентификатор камеры (формата cam{id}_(created_time)), время (Unix timestamp) начала записи, время окончания записи,
 * размер файла
 */
@Getter
public class Record {

    private int id;

    private String cameraId;

    private Integer startTimestamp;

    private Integer endTimestamp;

    private Integer fileSize;
}
