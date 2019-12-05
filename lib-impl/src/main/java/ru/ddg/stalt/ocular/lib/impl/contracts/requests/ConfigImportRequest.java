package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.OrganizationDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ConfigImportRequest extends BaseRequest {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrganizationDto> organizations;

    public ConfigImportRequest(UUID uuid, String server) {
        super(uuid, server);
    }
}
