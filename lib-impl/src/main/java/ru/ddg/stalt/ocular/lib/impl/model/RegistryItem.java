package ru.ddg.stalt.ocular.lib.impl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.BaseResponse;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public class RegistryItem<T extends BaseResponse> {
    private final UUID uuid;
    private final CompletableFuture<T> completableFuture;
    private final Class<T> responseClass;
    private final LocalDateTime expiredAt;
}
