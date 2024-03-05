package com.theinsideshine.insidesound.backend.albums.services;


import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;


public interface AlbumService {

    List<AlbumResponseDto> findAll();

     Optional<AlbumResponseDto> findById(Long id);


     Resource findImageById(Long id);

    List<AlbumResponseDto> findByUsername(String username);

     List<AlbumResponseDto> findPublicAlbumsByUsername(String username);
     AlbumResponseDto save(AlbumRequestDto albumRequestDto);
     AlbumResponseDto update(AlbumRequestDto albumRequestDto, Long id);

    void remove(Long id);

  /*  public void removeAlbumByUsername(String username);*/
}
