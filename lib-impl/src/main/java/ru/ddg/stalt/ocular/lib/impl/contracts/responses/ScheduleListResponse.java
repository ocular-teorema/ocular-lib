package ru.ddg.stalt.ocular.lib.impl.contracts.responses;

import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.ScheduleDto;

import java.util.List;
import java.util.Map;

@Getter
public class ScheduleListResponse extends BaseResponse<Map<String,List<ScheduleDto>>> {
}
