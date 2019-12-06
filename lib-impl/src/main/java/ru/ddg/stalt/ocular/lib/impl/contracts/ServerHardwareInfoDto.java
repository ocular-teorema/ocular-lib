package ru.ddg.stalt.ocular.lib.impl.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ServerHardwareInfoDto {

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("ocular_version")
    private String ocularVersion;

    private String uptime;

    @JsonProperty("load_average")
    private List<Double> loadAverage;

    @JsonProperty("cpu_utilization_perc")
    private Integer cpuUtilization;

    @JsonProperty(" disk_usage_perc")
    private Integer discUsage;

    @JsonProperty(" default_video_directory")
    private String defaultVideoPath;

}
