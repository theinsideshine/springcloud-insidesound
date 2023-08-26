package org.theinsideshine.insidesound.mvsc.albums.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;

import java.util.List;

public interface AlbumRepository extends CrudRepository <Album,Long> {


    @Query("SELECT a FROM Album a WHERE a.username = :username")
    List<Album> findByUsername(String username);

}
