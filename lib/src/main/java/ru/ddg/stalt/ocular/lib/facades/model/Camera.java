package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Данный объект содержит информацию о камере:
 * имя, адрес, время хранения видео (дней), адрес архива.
 */
@Getter
@AllArgsConstructor
public class Camera {

    String name;

    String address;

    Integer storageDays;

    String archivePath;
}
