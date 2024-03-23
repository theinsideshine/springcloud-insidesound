package com.theinsideshine.insidesound.backend.albums.models.dto;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;

public record AlbumResponseDto(
        Long id,
        String username,
        String title,
        String artist,
        String age,
        boolean albumprivate,

        Integer image

) {
    public static AlbumResponseDto albumResponseDtoMapperEntityToDto(Album album) {

        return new AlbumResponseDto(album.getId(),
                album.getUsername(),
                album.getTitle(),
                album.getArtist(),
                album.getAge(),
                album.isAlbumprivate(),
                album.getImageHashCode()
        );
    }


}
