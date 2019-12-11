package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.StorageDto;

import java.util.UUID;

@Getter
@Setter
public class StorageRequest extends BaseRequest {

    @JsonProperty("storage_id")
    private String storageId;

    private StorageDto storageDto;

    public StorageRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
