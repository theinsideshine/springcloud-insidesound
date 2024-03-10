package com.theinsideshine.insidesound.backend.tracks.services;


import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackServiceimpl implements TrackService {

    @Autowired
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    @Autowired
    public TrackServiceimpl(AlbumRepository albumRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponseDto> findAll() {
        List<Track> tracks =  trackRepository.findAll();
        return tracks.stream()
                .map(TrackResponseDto::trackResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource findImageById(Long id)  {
        Optional<Track> trackOptional = trackRepository.findById(id);
        if (trackOptional.isEmpty() || trackOptional.get().getImage() == null) {
            throw new InsidesoundException(InsidesoundErrorCode.IMG_NOT_FOUND_BY_TRACK_ID);
        }
        return new ByteArrayResource(trackOptional.get().getImage());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource findMp3ById(Long id)  {
        Optional<Track> trackOptional = trackRepository.findById(id);
        if (trackOptional.isEmpty() || trackOptional.get().getImage() == null) {
            throw new InsidesoundException(InsidesoundErrorCode.MP3_NOT_FOUND_BY_TRACK_ID);
        }
        return new ByteArrayResource(trackOptional.get().getMp3());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponseDto> findByAlbumId(Long id) {
        List<Track> tracks = trackRepository.findByAlbumId (id);
        if (tracks.size() == 0){
            throw new InsidesoundException(InsidesoundErrorCode.TRACK_NOT_FOUND_BY_ALBUM_ID);
        }
        return tracks.stream()
                .map(TrackResponseDto::trackResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
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

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponseDto> findByUsername(String username) {
        return trackRepository.findByUsername(username)
                .stream()
                .map(TrackResponseDto::trackResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrackResponseDto save(TrackRequestDto trackRequestDto)  {
        Track track =  TrackRequestDto.TrackRequestDtoMapperDtoToEntity(trackRequestDto);
        Track saveTrack = trackRepository.save(track);
        return TrackResponseDto.trackResponseDtoMapperEntityToDto(saveTrack);
    }

    @Override
    @Transactional
    public TrackResponseDto update(TrackRequestDto trackRequestDto, Long id) {
        validateTrackIdPost(id);
        Track trackToUpdate = TrackRequestDto.TrackRequestDtoMapperDtoToEntity(trackRequestDto);
        try {
            Track updateTrack = trackRepository.save(trackToUpdate);
            return TrackResponseDto.trackResponseDtoMapperEntityToDto(updateTrack);
        } catch (Exception e) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_UPDATING_TRACK);
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Optional<Track> o = trackRepository.findById(id);
        if (o.isPresent()) {
            try {
                trackRepository.deleteById(id);
            } catch (Exception e) {
                throw new InsidesoundException(InsidesoundErrorCode.ERR_DEL_TRACK);
            }
        }
    }

    @Override
    public Optional<Track> findById(Long id) {
        return trackRepository.findById(id);
    }


    @Transactional
    public void associateAlbumToTrack(Long trackId, Long albumId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new InsidesoundException(InsidesoundErrorCode.TRACK_ID_NOT_FOUND));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new InsidesoundException(InsidesoundErrorCode.ALBUM_ID_NOT_FOUND));
        // Asocia el álbum a la pista desde el lado de la pista
        track.setAlbum_id(album.getId());
        try {
            trackRepository.save(track);
        } catch (Exception e) {
            throw  new InsidesoundException(InsidesoundErrorCode.ERR_UPDATING_TRACK);
        }

    }
    private void validateTrackIdPost(Long id) {
        Optional<Track> optionalTrack= trackRepository.findById(id);
        if (optionalTrack.isEmpty()) {
            throw new InsidesoundException(InsidesoundErrorCode.ID_TRACK_NOT_FOUND);
        }

    }
}
