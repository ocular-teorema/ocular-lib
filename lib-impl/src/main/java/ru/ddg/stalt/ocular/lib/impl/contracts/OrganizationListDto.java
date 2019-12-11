package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.BaseResponse;

import java.util.List;

@Getter
public class OrganizationListDto extends BaseResponse {

    private List<OrganizationDto> organizations;
}
