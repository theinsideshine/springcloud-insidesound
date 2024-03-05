package com.theinsideshine.insidesound.backend.exceptions.dataintegrity;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Component
public class DataIntegrityViolationExceptionHandler {

    @Value("${errorMessages.userExists}")
    private String userExistsMessage;

    @Value("${errorMessages.emailExists}")
    private String emailExistsMessage;

    @Value("${errorMessages.titleExists}")
    private String titleExistsMessage;

    @Value("${errorMessages.integrityError}")
    private String integrityErrorMessage;

    public ResponseEntity<ErrorModel> handleException(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap = errorMapBuild(ex.getMessage());
        ErrorModel errorResponse = new ErrorModel(HttpStatus.CONFLICT.value(), errorMap);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    private Map<String, String> errorMapBuild(String errorMessage) {
        Map<String, String> errorMap = new HashMap<>();

        if (errorMessage.contains(".UK_")) {
            if (errorMessage.contains("users")) {
                if (errorMessage.contains("username")) {
                    errorMap.put("username", userExistsMessage);
                } else if (errorMessage.contains("email")) {
                    errorMap.put("email", emailExistsMessage);
                }
            } else if (errorMessage.contains("albums") || errorMessage.contains("tracks")) {
                if (errorMessage.contains("title")) {
                    errorMap.put("title", titleExistsMessage);
                }
            } else {
                errorMap.put("message", String.format(integrityErrorMessage, errorMessage));
            }
        }

        return errorMap;
    }

}
