package com.theinsideshine.insidesound.backend.exceptions.argumentnotvalid;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Component
public class MethodArgumentNotValidExceptionHandler {


    // Manejar excepciones de validación de bean para recordsDto

    public ResponseEntity<ErrorModel> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ErrorModel errorResponse = new ErrorModel(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
