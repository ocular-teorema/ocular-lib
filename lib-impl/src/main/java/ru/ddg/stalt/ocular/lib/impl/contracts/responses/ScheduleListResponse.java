package ru.ddg.stalt.ocular.lib.impl.contracts.responses;

import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.ScheduleDto;

import java.util.List;

@Getter
public class ScheduleListResponse extends BaseResponse<List<ScheduleDto>> {
}
