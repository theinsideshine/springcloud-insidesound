package com.theinsideshine.insidesound.backend.exceptions;


import java.util.Map;

public record ErrorModel(
        int errorCode,
        Map<String, String> errors
) {

}
