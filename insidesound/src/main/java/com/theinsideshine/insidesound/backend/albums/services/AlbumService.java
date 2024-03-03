package com.theinsideshine.insidesound.backend.albums.services;


import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface AlbumService {

    List<AlbumResponseDto> findAll();

    public Optional<AlbumResponseDto> findById(Long id);


    public Resource findImageById(Long id);

    List<AlbumResponseDto> findByUsername(String username);

    public List<AlbumResponseDto> findPublicAlbumsByUsername(String username);
    public AlbumResponseDto save(AlbumRequestDto albumRequestDto);
    public AlbumResponseDto update(AlbumRequestDto albumRequestDto, Long id);

    void remove(Long id);

    public void removeAlbumByUsername(String username);
}
