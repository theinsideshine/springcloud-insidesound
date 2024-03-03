package com.theinsideshine.insidesound.backend.exceptions.multipartfile;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Component
public class MaxUploadSizeExceededExceptionHandler {


    public ResponseEntity<ErrorModel> handleException(MaxUploadSizeExceededException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap = errorMapBuild(ex);
        ErrorModel errorResponse = new ErrorModel(HttpStatus.CONFLICT.value(), errorMap);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    private Map<String, String> errorMapBuild(MaxUploadSizeExceededException ex) {
        Map<String, String> errorMap = new HashMap<>();

        // Verifica si el mensaje contiene "mp3File"
        if (ex.getCause().getMessage().contains("mp3File")) {
            errorMap.put("mp3File", "El tamaño del archivo mp3 excede el límite máximo permitido.");
        }

        // Verifica si el mensaje contiene "imageFile"
        if (ex.getCause().getMessage().contains("imageFile")) {
            errorMap.put("imageFile", "El tamaño del archivo de imagen excede el límite máximo permitido.");
        }
        return errorMap;
    }

}

