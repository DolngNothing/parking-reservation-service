package com.oocl.parkingreservationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class GlobalException {
    @ExceptionHandler(ParkingOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String ParkingOrderException(ParkingOrderException e) {
        return e.getMessage();
    }
}
