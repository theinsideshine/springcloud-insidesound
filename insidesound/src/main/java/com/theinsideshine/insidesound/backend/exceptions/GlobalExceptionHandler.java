package com.theinsideshine.insidesound.backend.exceptions;

import com.theinsideshine.insidesound.backend.exceptions.argumentnotvalid.MethodArgumentNotValidExceptionHandler;
import com.theinsideshine.insidesound.backend.exceptions.dataintegrity.DataIntegrityViolationExceptionHandler;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final InsidesoundExceptionHandler insidesoundExceptionHandler;
   private final DataIntegrityViolationExceptionHandler dataIntegrityViolationExceptionHandler;
   private final MethodArgumentNotValidExceptionHandler methodArgumentNotValidExceptionHandler;

    // Constructor para la inyección de dependencias
    public GlobalExceptionHandler(
            InsidesoundExceptionHandler insidesoundExceptionHandler
            ,DataIntegrityViolationExceptionHandler dataIntegrityViolationExceptionHandler,
            MethodArgumentNotValidExceptionHandler methodArgumentNotValidExceptionHandler) {
       this.insidesoundExceptionHandler = insidesoundExceptionHandler;
       this.dataIntegrityViolationExceptionHandler = dataIntegrityViolationExceptionHandler;
       this.methodArgumentNotValidExceptionHandler = methodArgumentNotValidExceptionHandler;
    }

    // Maneja excepciones personalizadas de InsidesoundException
    @ExceptionHandler(InsidesoundException.class)
    public ResponseEntity<ErrorModel> handleCustomErrorException(InsidesoundException ex) {
        return insidesoundExceptionHandler.handleException(ex);
    }

    // Maneja excepciones de violación de integridad de datos
   @ExceptionHandler({DataIntegrityViolationException.class, DuplicateKeyException.class})
    public ResponseEntity<ErrorModel> handleDataIntegrityViolationException(Exception ex) {
        return dataIntegrityViolationExceptionHandler.handleException((DataIntegrityViolationException) ex);
    }
    // Maneja excepciones de validación de bean para recordsDto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorModel> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return methodArgumentNotValidExceptionHandler.handleException(ex);
    }
}
