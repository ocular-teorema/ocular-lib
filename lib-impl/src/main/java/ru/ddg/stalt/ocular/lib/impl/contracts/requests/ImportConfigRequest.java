package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.OrganizationDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ImportConfigRequest extends BaseRequest {

    private List<OrganizationDto> organizations;

    public ImportConfigRequest(UUID uuid, String server, String type) {
        super(uuid, server, type);
    }
}
