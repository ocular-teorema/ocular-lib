package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;

import java.util.List;

@Getter
public class ServerStateDto extends BaseResponse {

    private ServerHardwareInfoDto hardware;

    private List<CameraStateDto> cameras;

    private List<ServiceStateDto> services;
}
