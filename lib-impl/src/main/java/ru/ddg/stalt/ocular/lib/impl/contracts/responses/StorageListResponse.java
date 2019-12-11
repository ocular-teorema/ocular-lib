package ru.ddg.stalt.ocular.lib.impl.contracts.responses;

import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.StorageDto;

import java.util.List;

@Getter
public class StorageListResponse extends BaseResponse<List<StorageDto>> {
}
