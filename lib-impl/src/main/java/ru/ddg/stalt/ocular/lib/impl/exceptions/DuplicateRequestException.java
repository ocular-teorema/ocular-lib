package ru.ddg.stalt.ocular.lib.impl.exceptions;

import java.util.UUID;

public class DuplicateRequestException extends Exception {
    public DuplicateRequestException(UUID uuid, Class responseClass) {
        super("Duplicate request registered! Uuid: " + uuid + ", response class: " + responseClass);
    }
}
