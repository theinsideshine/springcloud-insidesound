package com.theinsideshine.insidesound.backend.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class InsidesoundExceptionHandler {

    @ExceptionHandler(InsidesoundException.class)
    public ResponseEntity<ErrorModel> handleCustomErrorException(InsidesoundException ex) {
        ErrorModel errorResponse = new ErrorModel(ex.getStatusCode(), ex.getErrorMap());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, DuplicateKeyException.class})
    public ResponseEntity<ErrorModel> handleDataIntegrityViolationException(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap = errorMapBuild(ex.getMessage());
        ErrorModel errorResponse = new ErrorModel(HttpStatus.CONFLICT.value(), errorMap);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Manejar excepciones de validación de bean para recordsDto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorModel> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ErrorModel errorResponse = new ErrorModel(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }



    private Map<String, String> errorMapBuild(String errorMessage) {
        Map<String, String> errorMap = new HashMap<>();

        if (errorMessage.contains("users") && errorMessage.contains(".UK_")) {
            if (errorMessage.contains("username")) {
                errorMap.put("username", "El username ya existe");
            } else if (errorMessage.contains("email")) {
                errorMap.put("email", "El email ya existe");
            }
        } else {
            errorMap.put("message", "Error de integridad de datos: " + errorMessage);
        }

        return errorMap;
    }

}
