package com.theinsideshine.insidesound.backend.tracks.services;


import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;


public interface TrackService {
    List<TrackResponseDto> findAll();
    Resource findMp3ById(Long id);
    Resource findImageById(Long id);
    List<TrackResponseDto> findByAlbumId(Long id);
    Long getAlbumIdByTrackId(Long trackId);
    List<TrackResponseDto> findByUsername(String username);
    TrackResponseDto save(TrackRequestDto trackRequestDto);
    TrackResponseDto update(TrackRequestDto trackRequestDto, Long id);
    void remove(Long id);
    void associateAlbumToTrack(Long albumId, Long trackId);
}
