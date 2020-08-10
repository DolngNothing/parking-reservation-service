package com.oocl.parkingreservationservice.exception;

import com.oocl.parkingreservationservice.constants.MessageConstants;

public class IllegalOrderOperationException extends Exception{
    private String exceptionMessage;
    public IllegalOrderOperationException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
