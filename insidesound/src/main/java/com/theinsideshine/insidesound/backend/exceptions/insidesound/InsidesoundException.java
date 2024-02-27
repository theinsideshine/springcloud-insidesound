package com.theinsideshine.insidesound.backend.exceptions.insidesound;

import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;

import java.util.Iterator;
import java.util.Map;

public class InsidesoundException extends RuntimeException {

    private final int statusCode;
    private final Map<String, String> errorMap;

    public InsidesoundException(int statusCode, Map<String, String> errorMap) {
        super(errorMap.toString());
        this.statusCode = statusCode;
        this.errorMap = errorMap;
    }

    public InsidesoundException(InsidesoundErrorCode errorCode) {
        //super(errorCode.getErrorCode() + ": " + errorCode.getErrorMap().values().iterator().next());
        super(buildErrorMessage(errorCode.getErrorMap()));
        this.statusCode = errorCode.getErrorCode();
        this.errorMap = errorCode.getErrorMap();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    private static String buildErrorMessage(Map<String, String> errorMap) {
        StringBuilder errorMessage = new StringBuilder();

        errorMessage.append("{");

        Iterator<Map.Entry<String, String>> iterator = errorMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            errorMessage.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue());

            if (iterator.hasNext()) {
                errorMessage.append(", ");
            }
        }

        errorMessage.append("}");

        return errorMessage.toString();
    }
}



