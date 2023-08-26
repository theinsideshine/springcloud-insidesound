package org.theinsideshine.insidesound.mvsc.albums.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


import java.util.List;

@Entity
@Table(name="albums")
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
    @Size(min = 4, max = 20)
    private String artist;
    private String age;

    @NotEmpty
    @JsonIgnore
    @Lob
    @Column(length = 1048576)
    private byte[]  image;

    @ElementCollection
    @CollectionTable(name = "album_tracks", joinColumns = @JoinColumn(name = "album_id"))
    @Column(name = "track_id")
    private List<Long> tracksId;
    public Album() {
    }

    public Album(Long id, String username, String title, String artist, String age, byte[] image, List<Long> tracksId) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.artist = artist;
        this.age = age;
        this.image = image;
        this.tracksId = tracksId;
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


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Long> getTracksId() {
        return tracksId;
    }

    public void setTracksId(List<Long> tracksId) {
        this.tracksId = tracksId;
    }

    public Integer getImageHashCode() {
        return ( this.image != null ) ? this.image.hashCode() : null ;
    }
}
