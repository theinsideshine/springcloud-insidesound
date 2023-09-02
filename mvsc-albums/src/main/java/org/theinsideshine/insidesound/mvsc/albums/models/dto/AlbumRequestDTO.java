package org.theinsideshine.insidesound.mvsc.albums.models.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class AlbumRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String title;

    @NotBlank
    private String artist;

    private String age;

    private boolean albumprivate;


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

