package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.Getter;

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
