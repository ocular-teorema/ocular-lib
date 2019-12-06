package ru.ddg.stalt.ocular.lib.impl.contracts.events;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CameraEvent {

    private UUID uuid;

    private String type = "cameras_events";

    private String camera_id;

    private Event event;
}
