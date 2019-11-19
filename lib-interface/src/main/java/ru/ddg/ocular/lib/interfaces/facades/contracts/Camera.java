package ru.ddg.ocular.lib.interfaces.facades.contracts;

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
