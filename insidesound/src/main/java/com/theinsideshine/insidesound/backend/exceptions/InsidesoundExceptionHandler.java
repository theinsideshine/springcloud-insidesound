package com.theinsideshine.insidesound.backend.exceptions;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InsidesoundExceptionHandler {

    @ExceptionHandler(InsidesoundException.class)
    public ResponseEntity<ErrorModel> handleCustomErrorException(InsidesoundException ex) {
        ErrorModel errorResponse = new ErrorModel(ex.getStatusCode(), ex.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}

