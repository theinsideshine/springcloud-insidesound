package com.theinsideshine.insidesound.backend.exceptions;



import org.springframework.http.ResponseEntity;

public interface ExceptionHandler<T extends Exception> {
    ResponseEntity<ErrorModel> handleException(T ex);
}
