package ru.ddg.stalt.ocular.lib.exceptions;

public class DuplicateDriverIdException extends BaseException {
    public DuplicateDriverIdException(String driverId) {
        super("DriverId " + driverId + " already exists.");
    }
}
