package ru.ddg.stalt.ocular.lib.impl.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.BaseResponse;
import ru.ddg.stalt.ocular.lib.impl.exceptions.DuplicateRequestException;
import ru.ddg.stalt.ocular.lib.impl.exceptions.RequestNotFoundException;
import ru.ddg.stalt.ocular.lib.impl.model.RegistryItem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestRegistry {
    private final ConcurrentHashMap<UUID, RegistryItem> registry = new ConcurrentHashMap<>();
    private final TreeMap<LocalDateTime, Set<UUID>> timeouts = new TreeMap<>();

    @Autowired
    private TimeService timeService;

    public <T extends BaseResponse> void register(UUID uuid, CompletableFuture<T> future, Class<T> responseClass) throws DuplicateRequestException {
        final LocalDateTime now = timeService.getUTC();
        final RegistryItem item = registry.putIfAbsent(uuid, new RegistryItem<>(
                uuid,
                future,
                responseClass,
                now
        ));

        if (item != null) {
            throw new DuplicateRequestException(uuid, responseClass);
        }

        synchronized (timeouts) {
            timeouts.compute(now, (localDateTime, uuids) -> {
                if (uuids == null) {
                    uuids = new HashSet<>();
                }
                uuids.add(uuid);
                return uuids;
            });
        }
    }

    public <T extends BaseResponse> void response(UUID uuid, T response) throws RequestNotFoundException {
        @SuppressWarnings("unchecked")
        RegistryItem<T> registryItem = registry.remove(uuid);
        if (registryItem == null) {
            throw new RequestNotFoundException("There is not request in registry for uuid " + uuid);
        }
        if (!registryItem.getResponseClass().isAssignableFrom(response.getClass())) {
            throw new ClassCastException("Response class does not corresponds with request. Response " + response.getClass() + ", but expected " + registryItem.getResponseClass());
        }
        registryItem.getCompletableFuture().complete(response);

        cleanup();
    }

    public void cleanup() {
        final LocalDateTime now = timeService.getUTC();
        Set<UUID> uuids;
        synchronized (timeouts) {
            uuids = timeouts.tailMap(now)
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            timeouts.tailMap(now).clear();
        }

        uuids.forEach(this::sendTimeoutException);
    }

    @Scheduled(fixedDelayString = "${ru.ddg.stalt.ocular.lib.clean-up-delay}")
    protected void regularCleanup() {
        cleanup();
    }

    private void sendTimeoutException(UUID uuid) {
        final RegistryItem item = registry.get(uuid);
        if (item == null) {
            return;
        }

        item.getCompletableFuture().completeExceptionally(new TimeoutException("There is not response until " + item.getExpiredAt() + " for request " + item.getUuid()));
    }
}
