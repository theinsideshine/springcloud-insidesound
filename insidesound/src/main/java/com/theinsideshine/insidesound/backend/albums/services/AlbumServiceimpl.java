package com.theinsideshine.insidesound.backend.albums.services;

import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

;

@Service
public class AlbumServiceimpl implements AlbumService{


    private final AlbumRepository albumRepository;

    private final TrackRepository trackRepository;

    @Autowired
    public AlbumServiceimpl(AlbumRepository albumRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDto> findAll() {
        List<Album> albums = (List<Album>) albumRepository.findAll();
        return albums.stream()
                .map(AlbumResponseDto::albumResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlbumResponseDto> findById(Long id) {
        return albumRepository.findById(id).map(AlbumResponseDto::albumResponseDtoMapperEntityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource findImageById(Long id)  {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isEmpty() || albumOptional.get().getImage() == null) {
            throw new InsidesoundException(InsidesoundErrorCode.IMG_ID_NOT_FOUND);
        }
        Resource image = new ByteArrayResource(albumOptional.get().getImage());
        return image;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDto> findByUsername(String username) {
        List<Album> albums = albumRepository.findByUsername(username);
        if (albums.size() == 0){
            throw new InsidesoundException(InsidesoundErrorCode.ALBUM_NOT_FOUND);
        }
        return albums.stream()
                .map(AlbumResponseDto::albumResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDto> findPublicAlbumsByUsername(String username) {
        List<Album> albums = albumRepository.findByUsernameAndAlbumprivateFalse(username);
        if (albums.size() == 0){
            throw new InsidesoundException(InsidesoundErrorCode.ALBUM_PUB_NOT_FOUND);
        }
        return albums.stream()
                .map(AlbumResponseDto::albumResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AlbumResponseDto save(AlbumRequestDto albumRequestDto)  {
         Album album =  AlbumRequestDto.AlbumRequestDtoMapperDtoToEntity(albumRequestDto);
        Album saveAlbum = albumRepository.save(album);
        return AlbumResponseDto.albumResponseDtoMapperEntityToDto(saveAlbum);
    }

    @Override
    @Transactional
    public AlbumResponseDto update(AlbumRequestDto albumRequestDto, Long id) {
        Album albumToUpdate = validateAlbumIdPost(id);
        albumToUpdate = AlbumRequestDto.AlbumRequestDtoMapperDtoToEntity(albumRequestDto);
        try {
            Album updateAlbum = albumRepository.save(albumToUpdate);
            return AlbumResponseDto.albumResponseDtoMapperEntityToDto(updateAlbum);
        } catch (Exception e) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_UPDATING_ALBUM);
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isPresent()) {
            Album album = albumOptional.get();
            removeAlbum(album);
            removeTracksByAlbumId(album.getId());
        }
    }

    private void removeAlbum(Album album) {
        try {
            albumRepository.deleteById(album.getId());
        } catch (Exception e) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_DEL_ALBUM);
        }
    }

    private void removeTracksByAlbumId(Long albumId) {
        try {
            trackRepository.removeTracksByAlbumId(albumId);
        } catch (Exception e) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_DEL_TRACKS_BY_ALBUM_ID);
        }
    }

    private Album validateAlbumIdPost(Long id) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if (optionalAlbum.isEmpty()) {
            throw new InsidesoundException(InsidesoundErrorCode.ID_ALBUM_NOT_FOUND);
        }
        return optionalAlbum.get();
    }
}
