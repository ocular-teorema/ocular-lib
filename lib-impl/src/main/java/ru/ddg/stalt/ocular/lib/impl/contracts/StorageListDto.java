package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;

import java.util.List;

@Getter
public class StorageListDto extends BaseResponse {
    private List<StorageDto> storages;
}
