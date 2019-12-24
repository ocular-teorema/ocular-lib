package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.UUID;

@Setter
public class DeleteStorageRequest extends BaseRequest {

    @JsonProperty("storage_id")
    private int storageId;

    public DeleteStorageRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
