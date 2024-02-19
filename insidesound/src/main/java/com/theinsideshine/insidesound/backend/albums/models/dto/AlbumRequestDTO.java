package com.theinsideshine.insidesound.backend.albums.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AlbumRequestDTO {
    @NotBlank
    @Size(min = 4, max = 8)
    private String username;

    @NotBlank
    @Size(min = 4, max = 20)
    private String title;

    @NotBlank
    @Size(min = 4, max = 23)
    private String artist;

    @Size(min = 3, max = 4)
    private String age;

    private boolean albumprivate;

    //No se pusieron Validacion para generar la estrateguia actual de manejo de errores
    private MultipartFile imageFile;

    public AlbumRequestDTO() {
    }

    public AlbumRequestDTO(String username, String title, String artist, String age, boolean albumprivate, MultipartFile imageFile) {
        this.username = username;
        this.title = title;
        this.artist = artist;
        this.age = age;
        this.albumprivate = albumprivate;
        this.imageFile = imageFile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isAlbumprivate() {
        return albumprivate;
    }

    public void setAlbumprivate(boolean albumprivate) {
        this.albumprivate = albumprivate;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}

