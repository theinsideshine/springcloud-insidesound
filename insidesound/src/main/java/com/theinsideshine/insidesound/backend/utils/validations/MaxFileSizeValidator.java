package com.theinsideshine.insidesound.backend.utils.validations;

import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        // Obtener el nombre del campo desde el contexto de validación
        String message = (String) context.getDefaultConstraintMessageTemplate();



        // Verificar si el archivo es nulo o vacío
        if (file == null || file.isEmpty()) {
            throw new InsidesoundException(HttpStatus.BAD_REQUEST.value(), Map.of(message, "No puede ser nulo ni estar vacio"));
        }
        // Verificar si el tamaño excede el límite máximo
        if (file.getSize() > maxSize) {
            throw new InsidesoundException(HttpStatus.BAD_REQUEST.value(),
                    Map.of(message,  buildErrorMessage(maxSize)));
        }
        // Si pasa ambas validaciones, el archivo es válido
        return true;
    }


    private String buildErrorMessage(long maxSize) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Excede su tamaño maximo de ").append(maxSize).append(" bytes");
        return errorMessage.toString();
    }






}

