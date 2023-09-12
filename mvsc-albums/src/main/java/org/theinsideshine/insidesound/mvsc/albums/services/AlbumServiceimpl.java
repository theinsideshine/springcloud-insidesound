package org.theinsideshine.insidesound.mvsc.albums.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;
import org.theinsideshine.insidesound.mvsc.albums.repositories.AlbumRepository;
import org.theinsideshine.insidesound.mvsc.albums.repositories.TrackRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        //albums.forEach(album -> album.getTracks().clear());  // Saca los tracks de albums no esta funcionando el .LAZY
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

        trackRepository.removeTracksByAlbumId(id);
        albumRepository.deleteById(id);
    }
}
