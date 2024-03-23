package com.theinsideshine.insidesound.backend.albums.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 8)
    private String username;
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(unique = true)
    private String title;
    @NotBlank
    @Size(min = 4, max = 23)
    private String artist;

    @Size(min = 3, max = 4)
    private String age;

    private boolean albumprivate;


    @NotEmpty
    @JsonIgnore
    @Lob
    private byte[] image;

    public Album() {
    }


    public Album(Long id, String username, String title, String artist, String age, boolean albumprivate, @NotEmpty byte[] image) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.artist = artist;
        this.age = age;
        this.albumprivate = albumprivate;
        this.image = image;
    }


    public Album(String username, String title, String artist, String age, boolean albumprivate, @NotEmpty byte[] image) {

        this.username = username;
        this.title = title;
        this.artist = artist;
        this.age = age;
        this.albumprivate = albumprivate;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getImageHashCode() {
        return (this.image != null) ? this.image.hashCode() : null;
    }
}
