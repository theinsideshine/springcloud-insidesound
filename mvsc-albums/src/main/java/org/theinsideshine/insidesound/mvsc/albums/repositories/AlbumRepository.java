package org.theinsideshine.insidesound.mvsc.albums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;


import java.util.List;

public interface AlbumRepository extends JpaRepository<Album,Long> {


    @Query("SELECT a FROM Album a WHERE a.username = :username")
    List<Album> findByUsername(String username);

    List<Album> findByUsernameAndAlbumprivateFalse(String username);

}
