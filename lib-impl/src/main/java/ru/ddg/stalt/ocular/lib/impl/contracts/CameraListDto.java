package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;

import java.util.List;

@Getter
public class CameraListDto extends BaseResponse {
    List<CameraDto> cameras;
}
