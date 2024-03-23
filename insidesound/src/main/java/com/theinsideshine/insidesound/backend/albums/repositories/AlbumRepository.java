package com.theinsideshine.insidesound.backend.albums.repositories;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {


    @Query("SELECT a FROM Album a WHERE a.username = :username")
    List<Album> findByUsername(String username);

    List<Album> findByUsernameAndAlbumprivateFalse(String username);

}
