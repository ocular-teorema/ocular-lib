package ru.ddg.stalt.ocular.lib.impl.contracts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrganizationDto extends BaseResponse {

    private String name;

    private List<ServerDto> servers;
}
