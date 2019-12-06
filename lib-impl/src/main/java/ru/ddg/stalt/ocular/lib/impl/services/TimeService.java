package ru.ddg.stalt.ocular.lib.impl.services;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class TimeService {
    public LocalDateTime getUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
