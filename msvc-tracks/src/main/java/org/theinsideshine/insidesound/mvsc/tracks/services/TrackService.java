package org.theinsideshine.insidesound.mvsc.tracks.services;

import org.springframework.transaction.annotation.Transactional;
import org.theinsideshine.insidesound.mvsc.tracks.models.entity.Track;


import java.util.List;
import java.util.Optional;


public interface TrackService {

    List<Track> findAll();

    List<Track> findByAlbumId(Long id);


    List<Track> findByUsername(String username);

    Optional<Track> findById(Long id);

    public Long getAlbumIdByTrackId(Long trackId);

    public void associateAlbumToTrack(Long albumId, Long trackId);

    public Track save(Track track);


    public void remove(Long id);

    public void removeTracksByAlbumId(Long albumId);


}
