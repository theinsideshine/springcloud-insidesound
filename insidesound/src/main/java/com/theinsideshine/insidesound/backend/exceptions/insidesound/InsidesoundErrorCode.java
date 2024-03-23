package com.theinsideshine.insidesound.backend.exceptions.insidesound;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;

import java.util.Map;

public enum InsidesoundErrorCode {


    /*
        Errores de user
     */
    ID_USER_NOT_FOUND(400, Map.of("PUT", "El usuario a editar no existe.")),
    ERR_UPDATING_USER(500, Map.of("PUT", "El usuario no se pudo editar.")),
    ERR_DEL_USER(500, Map.of("DELETE", "No se pudo borrar el usuario.")),
    ERR_DEL_ALBUM_BY_USERNAME(500, Map.of("DELETE ALBUM BY USERNAME", "No se pudo borrar el album asociado al username.")),
    ERR_DEL_TRACKS_BY_USERNAME(500, Map.of("DELETE TRACK BY USERNAME", "No se pudo borrar el track asociado al username.")),
    ERR_USER_NULL(500, Map.of("User entity", "El usuario no puede ser nulo.")),

    /*
        Errores de album
     */

    ALBUM_ID_NOT_FOUND(400, Map.of("FIND ALBUM BY ID", "El id no tiene album.")),
    IMG_NOT_FOUND_BY_ALBUM_ID(400, Map.of("GET IMAGES", "El id del album no tiene imagen.")),
    ALBUM_NOT_FOUND_BY_USERNAME(400, Map.of("GET ALBUMS BY USERNAME", "El username no tiene albums.")),
    ALBUM_PUBLIC_NOT_FOUND_BY_USERNAME(400, Map.of("GET ALBUMS BY USERNAME", "El username no tiene albums publicos.")),
    ID_ALBUM_NOT_FOUND(400, Map.of("PUT", "El album a editar no existe.")),

    ERR_CREATE_ALBUM(500, Map.of("POST", "El album no se pudo crear.")),
    ERR_UPDATING_ALBUM(500, Map.of("PUT", "El album no se pudo editar.")),
    ERR_DEL_ALBUM(500, Map.of("DELETE", "No se pudo borrar el album.")),
    ERR_DEL_TRACKS_BY_ALBUM_ID(500, Map.of("DELETE TRACKS BY ALBUM ID", "No se pudo borrar el track asociado al album.")),

    /*
        Errores de track
     */

    TRACK_ID_NOT_FOUND(400, Map.of("FIND TRACK BY ID", "El id no tiene track.")),

    TRACK_NOT_FOUND_BY_ALBUM_ID(400, Map.of("FIND TRACK BY ID", "No existe track con el albumId asociado.")),
    IMG_NOT_FOUND_BY_TRACK_ID(400, Map.of("GET IMAGES", "El id del track no tiene imagen.")),
    MP3_NOT_FOUND_BY_TRACK_ID(400, Map.of("GET MP3", "El id del track no tiene mp3.")),
    ID_TRACK_NOT_FOUND(400, Map.of("PUT", "El track a editar no existe.")),
    ERR_UPDATING_TRACK(500, Map.of("PUT", "El track no se pudo editar.")),
    ERR_DEL_TRACK(500, Map.of("DELETE", "No se pudo borrar el track.")),
    /*
      Errores de conversion
    */
    ERR_CONV_IMAGE(400, Map.of("CONVERT BYTES IMAGEFILE", "No se pudo realizar de la conversion.")),
    ERR_CONV_MULTIPARTFILE(400, Map.of("CONVERT BYTES IMAGE-MP3/FILE", "No se pudo realizar de la conversion."));


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

    public String getValueMapErrorMessage() {
        if (!errorMap.isEmpty()) {
            return errorMap.values().iterator().next();
        }
        return null; // O cualquier otro valor predeterminado que desees retornar
    }
}

