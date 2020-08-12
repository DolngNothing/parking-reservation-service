package com.oocl.parkingreservationservice.handler;


import com.oocl.parkingreservationservice.dto.ExceptionBean;
import com.oocl.parkingreservationservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.oocl.parkingreservationservice.constants.MessageConstants.*;

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
    public ExceptionBean illegalOrderOperationException(IllegalOrderOperationException illegalOrderOperationException) {
        return new ExceptionBean(illegalOrderOperationException.getMessage());
    }

    @ExceptionHandler(OrderNotExistException.class)
    public ExceptionBean orderNotExistException(OrderNotExistException orderNotExistException) {
        return new ExceptionBean(orderNotExistException.getMessage());
    }

    @ExceptionHandler(ParkingOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String parkingOrderException(ParkingOrderException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBean illegalParameterException(IllegalParameterException e) {
        return new ExceptionBean(e.getMessage());
    }

    @ExceptionHandler(InquiryOrderException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBean inquiryOrderException(UserNotExistException e) {
        return new ExceptionBean(e.getMessage());
    }

    @ExceptionHandler(UserNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBean userNotExist() {
        return new ExceptionBean(USER_NOT_EXIST);
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionBean notLogin() {
        return new ExceptionBean(UN_LOGIN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBean unknownException() {
        return new ExceptionBean(UNKNOWN_EXCEPTION);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBean NoAuthorityException() {
        return new ExceptionBean(NO_AUTHORITY);
    }
}
