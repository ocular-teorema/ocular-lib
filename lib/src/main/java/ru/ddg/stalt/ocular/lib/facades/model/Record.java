package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

/**
 * Данный объект содержит информацию о записи камеры:
 * идентификатор записи, идентификатор камеры, время начала записи, время окончания записи,
 * размер файла
 */
@Getter
public class Record {

    int id;

    String cameraId;

    Integer startTimestamp;

    Integer endTimestamp;

    Integer fileSize;
}
