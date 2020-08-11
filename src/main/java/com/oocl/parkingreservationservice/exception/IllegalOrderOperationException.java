package com.oocl.parkingreservationservice.exception;

public class IllegalOrderOperationException extends Exception {
    private final String exceptionMessage;

    public IllegalOrderOperationException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
