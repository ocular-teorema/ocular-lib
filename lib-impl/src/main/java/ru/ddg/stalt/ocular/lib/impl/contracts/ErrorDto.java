package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDto {
    private boolean success;
    @JsonProperty("code")
    private int errorCode;
    @JsonProperty("error")
    private String errorDescription;
}
