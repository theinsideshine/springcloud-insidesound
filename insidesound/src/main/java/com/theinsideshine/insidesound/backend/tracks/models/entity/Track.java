package com.theinsideshine.insidesound.backend.tracks.models.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 8)
    private String username;

    @NotBlank
    @Size(min = 4, max = 30)
    @Column(unique = true)
    private String title;

    @NotEmpty
    @JsonIgnore
    @Lob
    /*@Column(length = 1048576)*/
    private byte[]  image;

    @NotEmpty
    @JsonIgnore
    @Lob
    /*@Column(length =  10485760)*/
    private byte[]  mp3;


    @Column(name = "album_id", columnDefinition = "BIGINT default 0")
    private Long album_id;

    public Track() {
    }

    public Track(Long id, String username, String title, byte[] image, byte[] mp3, Long album_id) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.image = image;
        this.mp3 = mp3;
        this.album_id = album_id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getMp3() {
        return mp3;
    }

    public void setMp3(byte[] mp3) {
        this.mp3 = mp3;
    }

    public Long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(Long album_id) {
        this.album_id = album_id;
    }

    public Integer getImageHashCode() {
        return ( this.image != null ) ? this.image.hashCode() : null ;
    }

    public Integer getMp3HashCode() {
        return ( this.mp3 != null ) ? this.mp3.hashCode() : null ;
    }
}



