package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.StorageDto;

import java.util.UUID;

@Setter
public class UpdateStorageRequest extends BaseRequest {

    @JsonProperty("storage_id")
    private String storageId;

    @JsonProperty("data")
    private StorageDto storageDto;

    public UpdateStorageRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
