package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceState {
    private String name;

    private String status;

    private Integer errorCode;

    private String errorMessage;
}
