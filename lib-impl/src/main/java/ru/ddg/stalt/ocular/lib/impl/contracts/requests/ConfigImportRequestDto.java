package ru.ddg.stalt.ocular.lib.impl.contracts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.ddg.stalt.ocular.lib.impl.contracts.OrganizationDto;

import java.util.List;

@Getter
@Setter
public class ConfigImportRequestDto {

    @JsonProperty("request_uid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer requestUid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrganizationDto> organizations;
}
