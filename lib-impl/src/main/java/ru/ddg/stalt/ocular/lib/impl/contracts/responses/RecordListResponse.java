package ru.ddg.stalt.ocular.lib.impl.contracts.responses;

import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.RecordDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.BaseResponse;

import java.util.List;

@Getter
public class RecordListResponse extends BaseResponse<List<RecordDto>> {
}
