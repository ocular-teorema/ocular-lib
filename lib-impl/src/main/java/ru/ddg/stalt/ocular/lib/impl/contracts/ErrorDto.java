package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;

@Getter
public class ErrorDto {
    private Boolean success;

    private String error;

    private Integer code;
}
