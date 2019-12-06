package ru.ddg.stalt.ocular.lib.impl.contracts.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Event implements OcularEvent {

    private Integer eventId;

    @JsonProperty("event_start_timestamp")
    private Integer eventStartTimestamp;

    @JsonProperty("event_end_timestamp")
    private Integer eventEndTimestamp;

    @JsonProperty("event_type")
    private Integer eventType;

    private Integer confidence;

    private Integer reaction;
}
