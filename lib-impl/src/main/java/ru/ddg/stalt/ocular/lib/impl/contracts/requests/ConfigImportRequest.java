package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.model.Organization;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ConfigImportRequest extends BaseRequest {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Organization> organizations;

    public ConfigImportRequest(UUID uuid, String server, String type) {
        super(uuid, server, type);
    }
}
