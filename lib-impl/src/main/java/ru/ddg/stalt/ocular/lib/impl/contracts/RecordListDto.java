package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;

import java.util.List;

@Getter
public class RecordListDto extends BaseResponse {
    private List<RecordDto> records;
}
