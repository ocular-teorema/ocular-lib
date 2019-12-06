package ru.ddg.stalt.ocular.lib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Данный объект содержит информацию о сервере:
 * Информация об оборудовании, сервисах, список камер.
 */
@Getter
@Setter
@AllArgsConstructor
public class ServerState {

    private ServerHardwareInfo hardwareInfo;

    private List<ServiceState> services;

    private List<CameraState> cameras;
}
