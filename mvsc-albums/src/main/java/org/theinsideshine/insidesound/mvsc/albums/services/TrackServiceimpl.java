package org.theinsideshine.insidesound.mvsc.albums.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;
import org.theinsideshine.insidesound.mvsc.albums.repositories.AlbumRepository;
import org.theinsideshine.insidesound.mvsc.albums.repositories.TrackRepository;

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

    public Integer getAlbumIdByTrackId(Long trackId) {
        Optional<Track> optionalTrack = trackRepository.findById(trackId);
        if (optionalTrack.isPresent()) {
            Track track = optionalTrack.get();
            if (track.getAlbum() != null) {
                return Math.toIntExact(track.getAlbum().getId());
            }
        }
        return null;
    }

    @Transactional
    public void associateAlbumToTrack(Long trackId, Long albumId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Pista no encontrada"));

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Álbum no encontrado"));

        // Asocia el álbum a la pista desde el lado de la pista
        track.setAlbum(album);

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
}
