package org.theinsideshine.insidesound.mvsc.tracks.controllers;


/*
  Esta clase se usa para devolver un string cuando se llama un endPoint desde un clienFeing
  Si solo devuelvo un String se puede tener problemas en la conversion
 */
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

