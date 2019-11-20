package ru.ddg.ocular.lib.interfaces.facades.model;

import lombok.Getter;
import ru.ddg.ocular.lib.interfaces.facades.model.Server;

import java.util.List;

/**
 * Данный объект содержит информацию об организации:
 * имя, списрк серверов
 */
@Getter
public class Organization {

    String name;

    List<Server> servers;
}
