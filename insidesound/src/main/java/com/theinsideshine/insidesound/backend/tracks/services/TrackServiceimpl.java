package com.theinsideshine.insidesound.backend.tracks.services;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class TrackServiceimpl implements TrackService {


    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private TrackRepository trackRepository;




    @Override
    @Transactional(readOnly = true)
    public List<Track> findAll() {
        List<Track> tracks = (List<Track>) trackRepository.findAll();
        return tracks;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Track> findByAlbumId(Long id) {
        return trackRepository.findByAlbumId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Track> findByUsername(String username) {
        return trackRepository.findByUsername(username);
    }

    @Override
    public Optional<Track> findById(Long id) {
        return trackRepository.findById(id);
    }

    public Long getAlbumIdByTrackId(Long trackId) {
        Optional<Track> optionalTrack = trackRepository.findById(trackId);
        if (optionalTrack.isPresent()) {
            Track track = optionalTrack.get();
            if (track.getAlbum_id() != 0) {
                return (track.getAlbum_id());
            }
        }
        return null;
    }

    @Transactional
    public void associateAlbumToTrack(Long trackId, Long albumId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Pista no encontrada"));


        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album no encontrada"));


        // Asocia el álbum a la pista desde el lado de la pista
        track.setAlbum_id(album.getId());

        // Guarda los cambios en la base de datos
        trackRepository.save(track);

    }

    @Override
    @Transactional
    public Track save(Track track) {
        return trackRepository.save(track);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        trackRepository.deleteById(id);
    }

    @Modifying
    @Transactional
    public void removeTracksByAlbumId(Long albumId) {
        trackRepository.removeTracksByAlbumId(albumId);
    }
}
