package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.StorageDto;

import java.util.UUID;

@Getter
@Setter
public class AddStorageRequest extends BaseRequest {

    @JsonProperty("data")
    private StorageDto storageDto;

    public AddStorageRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
