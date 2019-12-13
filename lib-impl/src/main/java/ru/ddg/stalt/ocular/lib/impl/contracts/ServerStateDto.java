package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.BaseResponse;

import java.util.List;

@Getter
public class ServerStateDto {

    private ServerHardwareInfoDto hardware;

    private List<CameraStateDto> cameras;

    private List<ServiceStateDto> services;
}
