package com.theinsideshine.insidesound.backend.exceptions;

public record ErrorModel(
        int errorCode,
        String errorMessage
) {

}
