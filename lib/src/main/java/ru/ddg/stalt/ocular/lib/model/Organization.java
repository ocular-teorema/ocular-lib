package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;

import java.util.List;

/**
 * Данный объект содержит информацию об организации:
 * имя, списрк серверов
 */
@Getter
public class Organization {

    private String name;

    private List<Server> servers;
}
