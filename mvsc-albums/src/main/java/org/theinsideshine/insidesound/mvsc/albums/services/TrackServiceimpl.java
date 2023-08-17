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

@Service
public class TrackServiceimpl implements TrackService {

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
