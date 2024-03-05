package com.theinsideshine.insidesound.backend.exceptions.insidesound;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
@Component
public class InsidesoundExceptionHandler {

    public ResponseEntity<ErrorModel> handleException(InsidesoundException ex) {
        ErrorModel errorResponse = new ErrorModel(ex.getStatusCode(), ex.getErrorMap());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
