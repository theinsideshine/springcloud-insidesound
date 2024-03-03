package com.theinsideshine.insidesound.backend.exceptions.dataintegrity;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Component
public class DataIntegrityViolationExceptionHandler {

    public ResponseEntity<ErrorModel> handleException(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap = errorMapBuild(ex.getMessage());
        ErrorModel errorResponse = new ErrorModel(HttpStatus.CONFLICT.value(), errorMap);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    private Map<String, String> errorMapBuild(String errorMessage) {
        Map<String, String> errorMap = new HashMap<>();

        if (errorMessage.contains("users") && errorMessage.contains(".UK_")) {
            if (errorMessage.contains("username")) {
                errorMap.put("username", "El username ya existe");
            } else if (errorMessage.contains("email")) {
                errorMap.put("email", "El email ya existe");
            }
        } else if (errorMessage.contains("albums") && errorMessage.contains(".UK_")) {
            if (errorMessage.contains("title")) {
                errorMap.put("title", "El titulo ya existe");
            }
        }else if (errorMessage.contains("tracks") && errorMessage.contains(".UK_")) {
            if (errorMessage.contains("title")) {
                errorMap.put("title", "El titulo ya existe");
            }
        } else {
            errorMap.put("message", "Error de integridad de datos: " + errorMessage);
        }

        return errorMap;
    }

}
