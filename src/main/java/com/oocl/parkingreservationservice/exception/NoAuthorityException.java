package com.oocl.parkingreservationservice.exception;

import com.oocl.parkingreservationservice.constants.MessageConstants;

public class NoAuthorityException extends Exception{
    @Override
    public String getMessage() {
        return MessageConstants.NO_AUTHORITY;
    }
}
