package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * ip-адрес, версию ocular, время работы, среднюю загруженность, загрузку cpu и дисков,
 * директорию для видео по-умолчанию, уникальный идентификатор для отправки/приема.
 */
@Getter
@AllArgsConstructor
public class ServerState {

    String ipAddress;

    String ocularVersion;

    String uptime;

    List<Double> loadAverage;

    Integer cpuUtilization;

    Integer discUsage;

    String defaultArchivePath;
}
