package com.theinsideshine.insidesound.backend.tracks.services;


import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.Modifying;

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
        List<Track> tracks = (List<Track>) trackRepository.findAll();
        return tracks.stream()
                .map(TrackResponseDto::trackResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource findImageById(Long id)  {
        Optional<Track> trackOptional = trackRepository.findById(id);
        if (trackOptional.isEmpty() || trackOptional.get().getImage() == null) {
            throw new InsidesoundException(InsidesoundErrorCode.IMG_ID_NOT_FOUND);
        }
        Resource image = new ByteArrayResource(trackOptional.get().getImage());
        return image;
    }

    @Override
    @Transactional(readOnly = true)
    public Resource findMp3ById(Long id)  {
        Optional<Track> trackOptional = trackRepository.findById(id);
        if (trackOptional.isEmpty() || trackOptional.get().getImage() == null) {
            throw new InsidesoundException(InsidesoundErrorCode.MP3_ID_NOT_FOUND);
        }
        Resource mp3 = new ByteArrayResource(trackOptional.get().getMp3());
        return mp3;
    }


    @Override
    @Transactional(readOnly = true)
    public List<TrackResponseDto> findByAlbumId(Long id) {
        List<Track> tracks = trackRepository.findByAlbumId (id);
        if (tracks.size() == 0){
            throw new InsidesoundException(InsidesoundErrorCode.TRACK_NOT_FOUND);
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
    public Optional<Track> findById(Long id) {
        return trackRepository.findById(id);
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
    public void remove(Long id) {
        trackRepository.deleteById(id);
    }

    @Modifying
    @Transactional
    public void removeTracksByAlbumId(Long albumId) {
        trackRepository.removeTracksByAlbumId(albumId);
    }
}
