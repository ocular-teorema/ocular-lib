package ru.ddg.stalt.ocular.lib.impl.services;

import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.impl.model.RequestKey;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandRegistry {
    private final ConcurrentHashMap<UUID, RequestKey> registry = new ConcurrentHashMap<>();
    private final TreeMap<LocalDateTime, Set<UUID>> timeouts = new TreeMap<>();

    public void <T> register(UUID uuid, CompletableFuture<T> future, Class<T> responseClass)
}
