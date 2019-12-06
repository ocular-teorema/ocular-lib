package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Данный объект содержит информацию об организации:
 * имя, списрк серверов
 */
@Getter
@Setter
public class Organization {

    private String name;

    private List<Server> servers;
}
