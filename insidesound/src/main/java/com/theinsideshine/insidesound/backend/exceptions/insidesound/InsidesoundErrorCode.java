package com.theinsideshine.insidesound.backend.exceptions.insidesound;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;

import java.util.Map;

public enum InsidesoundErrorCode {

    ID_USER_NOT_FOUND(400, Map.of("PUT", "El usuario a editar no existe.")),

    ID_ALBUM_NOT_FOUND(400, Map.of("PUT", "El album a editar no existe.")),

    ERR_UPDATING_USER(500, Map.of("PUT", "El usuario no se pudo editar.")),

    ERR_UPDATING_ALBUM(500, Map.of("PUT", "El album no se pudo editar.")),

    ERR_USER_NULL(500, Map.of("User entity", "El usuario no puede ser nulo.")),

    IMG_ID_NOT_FOUND(400, Map.of("GET IMAGES", "El id no tiene imagen.")),

    MP3_ID_NOT_FOUND(400, Map.of("GET MP3", "El id no tiene mp3.")),

    ALBUM_NOT_FOUND(400, Map.of("GET ALBUMS BY USERNAME", "El username no tiene albums.")),


    TRACK_NOT_FOUND(400, Map.of("GET TRACK BY ALBUM_ID", "El album_id no tiene tracks.")),

    ALBUM_PUB_NOT_FOUND(400, Map.of("GET ALBUMS BY USERNAME", "El username no tiene albums publicos.")),


    ERR_CONV_IMAGE(400, Map.of("CONVERT BYTES IMAGEFILE", "No se pudo realizar de la conversion.")),


    ERR_CONV_MULTIPARTFILE(400, Map.of("CONVERT BYTES IMAGE-MP3/FILE", "No se pudo realizar de la conversion.")),

    ERR_DEL_USER(500, Map.of("DELETE", "No se pudo borrar el usuario.")),

    ERR_DEL_ALBUM(500, Map.of("DELETE", "No se pudo borrar el album.")),
    ERR_DEL_TRACKS_BY_ALBUM_ID(500, Map.of("DELETE TRACKS BY ALBUM ID", "No se pudo borrar el track asociado al album.")),

    ERR_DEL_ALBUM_BY_USERNAME(500, Map.of("DELETE ALBUM BY USERNAME", "No se pudo borrar el album asociado al username.")),


    MAX_IMAGEFILE_EXCEEDED(500, Map.of("VALID IMAGE FILE", "El tamano del archivo no puede ser superior a 1MB"));
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

