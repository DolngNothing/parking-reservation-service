package com.oocl.parkingreservationservice.controller;


import com.oocl.parkingreservationservice.dto.ExceptionBean;
import com.oocl.parkingreservationservice.exception.IllegalOrderOperationException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author XUAL7
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionBean illegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return new ExceptionBean(illegalArgumentException.getMessage());
    }
    @ExceptionHandler(IllegalOrderOperationException.class)
    public ExceptionBean  illegalOrderOperationException(IllegalOrderOperationException illegalOrderOperationException){
        return new ExceptionBean(illegalOrderOperationException.getMessage());
    }
    @ExceptionHandler(OrderNotExistException.class)
    public ExceptionBean orderNotExistException(OrderNotExistException orderNotExistException){
        return new ExceptionBean(orderNotExistException.getMessage());
    }
}
