package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * Информация об оборудовании, сервисах, список камер.
 */
@Getter
public class ServerState {

    private ServerHardwareInfo hardwareInfo;

    private List<Service> services;

    private List<Camera> cameras;
}
