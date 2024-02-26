package com.theinsideshine.insidesound.backend.exceptions;

import java.util.Map;

public enum InsidesoundErrorCode {

    ID_USER_NOT_FOUND(400, Map.of("PUT", "El usuario a editar no existe.")),

    ERR_UPDATING_USER(500, Map.of("PUT", "El usuario no se pudo editar.")),

    ERR_USER_NULL(500, Map.of("User entity", "El usuario no puede ser nulo.")),


    ERR_DEL_USER(500, Map.of("DELETE", "No se pudo borrar el usuario."));




    private final int errorCode;
    private final Map<String, String> errorMap;

    InsidesoundErrorCode(int errorCode, Map<String, String> errorMap) {
        this.errorCode = errorCode;
        this.errorMap = errorMap;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public ErrorModel toErrorModel() {
        return new ErrorModel(errorCode, errorMap);
    }
}

