package org.theinsideshine.insidesound.mvsc.tracks.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class TrackRequestDTO {
    @NotBlank
    @Size(min = 4, max = 8)
    private String username;

    @NotBlank
    @Size(min = 4, max = 30)
    private String title;

    //No se pusieron Validacion para generar la estrateguia actual de manejo de errores
    private MultipartFile imageFile;

    //No se pusieron Validacion para generar la estrateguia actual de manejo de errores
    private MultipartFile mp3File;

    public TrackRequestDTO() {
    }

    public TrackRequestDTO(String username, String title, MultipartFile imageFile, MultipartFile mp3File) {
        this.username = username;
        this.title = title;
        this.imageFile = imageFile;
        this.mp3File = mp3File;
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

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public MultipartFile getMp3File() {
        return mp3File;
    }

    public void setMp3File(MultipartFile mp3File) {
        this.mp3File = mp3File;
    }
}

