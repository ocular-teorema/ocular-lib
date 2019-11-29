package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * ip-адрес, версию ocular, время работы, среднюю загруженность, загрузку cpu и дисков,
 * директорию для видео по-умолчанию.
 */
@Getter
public class ServerHardwareInfo {

    private String ipAddress;

    private String ocularVersion;

    private String uptime;

    private List<Double> loadAverage;

    private Integer cpuUtilization;

    private Integer discUsage;

    private String defaultArchivePath;
}
