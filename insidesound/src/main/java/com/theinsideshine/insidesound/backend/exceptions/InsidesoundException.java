package com.theinsideshine.insidesound.backend.exceptions;

public class InsidesoundException extends RuntimeException {

    private final int statusCode;
    private final String errorMessage;

    public InsidesoundException(int statusCode, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public InsidesoundException(InsidesoundErrorCode errorCode) {
        super(errorCode.getReason());
        this.statusCode = errorCode.getCode();
        this.errorMessage = errorCode.getReason();
    }


    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}

