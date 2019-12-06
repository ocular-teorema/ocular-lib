package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServerDto {

    private String serverId;

    private String name;

    private List<CameraDto> cameras;

    private List<StorageDto> storages;

    private List<ScheduleDto> schedules;
}
