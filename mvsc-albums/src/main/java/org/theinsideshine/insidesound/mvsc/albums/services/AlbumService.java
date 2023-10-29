package org.theinsideshine.insidesound.mvsc.albums.services;

import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;

import java.util.List;
import java.util.Optional;


public interface AlbumService {

    List<Album> findAll();

    Optional<Album> findById(Long id);

    List<Album> findByUsername(String username);

    public List<Album> findPublicAlbumsByUsername(String username);
    Album save(Album album);
    Optional<Album> update(Album album, Long id);

    void remove(Long id);

    public void removeAlbumByUsername(Long id);
}
