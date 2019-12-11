package ru.ddg.stalt.ocular.lib.exceptions;

public class ErrorResponseException extends BaseException {
    public ErrorResponseException(Class requestClass, int code, String description) {
        super("Request " + requestClass + " was finished with error " + code + ": " + description);
    }
}
