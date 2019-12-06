package ru.ddg.stalt.ocular.lib.exceptions;

public class ResponseTimeoutException extends BaseException {
    public ResponseTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
