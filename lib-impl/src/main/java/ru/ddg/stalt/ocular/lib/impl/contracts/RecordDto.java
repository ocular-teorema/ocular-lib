package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;

@Getter
public class RecordDto {
    private Integer id;

    private String cameraId;

    private Integer startTimestamp;

    private Integer endTimestamp;

    private Integer fileSize;
}
