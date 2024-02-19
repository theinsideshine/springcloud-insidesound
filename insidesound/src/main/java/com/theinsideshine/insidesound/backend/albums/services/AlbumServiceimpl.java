package com.theinsideshine.insidesound.backend.albums.services;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceimpl implements AlbumService{

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Album> findAll() {

        List<Album> albums = (List<Album>) albumRepository.findAll();
        return albums;
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Album> findById(Long id) {
        return albumRepository.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Album> findByUsername(String username) {
        return albumRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Album> findPublicAlbumsByUsername(String username) {
        return albumRepository.findByUsernameAndAlbumprivateFalse(username);
    }
    @Override
    @Transactional
    public Album save(Album album) {
        return albumRepository.save(album);
    }
    @Override
    public Optional<Album> update(Album album, Long id) {
        Optional<Album> o = albumRepository.findById(id);
        Album albumOptional = null;
        if (o.isPresent()) {
            Album albumDb = o.orElseThrow();
            albumDb.setTitle(album.getTitle());
            albumDb.setArtist(album.getArtist());
            albumDb.setAge(album.getAge());
            albumOptional = albumRepository.save(albumDb);
        }


        return Optional.ofNullable(albumOptional);
    }

    @Override
    @Transactional
    public void remove(Long id) {

        albumRepository.deleteById(id);
        trackRepository.removeTracksByAlbumId(id);

    }

    // Este end point se llama cuando se borra un usuario en msvc-security
    // En el controler busca los id del username borrado
    @Override
    @Transactional
    public void removeAlbumByUsername(Long id) {

        // Luego, elimina el álbum
        albumRepository.deleteById(id);
    }
}
