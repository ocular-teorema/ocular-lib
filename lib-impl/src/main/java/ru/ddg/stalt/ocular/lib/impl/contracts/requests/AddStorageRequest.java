package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.StorageDto;

import java.util.UUID;

@Getter
@Setter
public class AddStorageRequest extends BaseRequest {

    private StorageDto storageDto;

    public AddStorageRequest(UUID uuid, String server, String type) {
        super(uuid, server, type);
    }
}
