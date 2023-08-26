package org.theinsideshine.insidesound.mvsc.albums.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tracks")
@JsonIgnoreProperties({"album"})
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 4, max = 8)
    private String username;

    @NotBlank
    @Size(min = 4, max = 20)
    @Column(unique = true)
    private String title;

    @NotEmpty
    @JsonIgnore
    @Lob
    @Column(length = 10485760)
    private byte[]  image;

    @NotEmpty
    @JsonIgnore
    @Lob
    @Column(length =  10485760)
    private byte[]  mp3;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private Album album;

    public Track() {
    }

    public Track(int id, String username, String title, byte[] image, byte[] mp3, Album album) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.image = image;
        this.mp3 = mp3;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Integer getImageHashCode() {
        return ( this.image != null ) ? this.image.hashCode() : null ;
    }

    public Integer getMp3HashCode() {
        return ( this.mp3 != null ) ? this.mp3.hashCode() : null ;
    }
}



