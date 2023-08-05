package org.theinsideshine.insidesound.mvsc.albums.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    @Lob
    @Column(length = 1048576)
    private byte[]  foto;

    public Image() {
    }

    public Image(Long id, String username, byte[] foto) {
        this.id = id;
        this.username = username;
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getImageHashCode() {
        return ( this.foto != null ) ? this.foto.hashCode() : null ;
    }
}
