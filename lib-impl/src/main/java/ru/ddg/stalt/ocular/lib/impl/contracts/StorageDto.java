package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageDto {

    private int id;

    private String name;

    @JsonProperty("default_archive_path")
    private String defaultArchivePath;
}