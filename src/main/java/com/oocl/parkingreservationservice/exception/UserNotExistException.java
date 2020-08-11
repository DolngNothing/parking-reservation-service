package com.oocl.parkingreservationservice.exception;

import com.oocl.parkingreservationservice.constants.MessageConstants;

public class UserNotExistException extends Exception{

    public UserNotExistException(String message) {
        super(message);
    }

}
