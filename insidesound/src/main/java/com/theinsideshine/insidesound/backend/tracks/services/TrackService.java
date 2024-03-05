package com.theinsideshine.insidesound.backend.tracks.services;



import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;


public interface TrackService {

    public List<TrackResponseDto> findAll();

    public List<TrackResponseDto> findByAlbumId(Long id);

    public Resource findMp3ById(Long id);

    public Resource findImageById(Long id);


    public List<TrackResponseDto> findByUsername(String username);


    public TrackResponseDto save(TrackRequestDto trackRequestDto) ;

    public TrackResponseDto update(TrackRequestDto trackRequestDto, Long id);

    public void remove(Long id);

    Optional<Track> findById(Long id);

    public Long getAlbumIdByTrackId(Long trackId);

    public void associateAlbumToTrack(Long albumId, Long trackId);





}
