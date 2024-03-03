package com.theinsideshine.insidesound.backend.albums.models.dto;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.users.models.entities.User;

public record AlbumResponseDto(
         Long id,
         String username,
         String title,
         String artist,
         String age ,
         boolean albumprivate,

         Integer image

) {
 public static AlbumResponseDto albumResponseDtoMapperEntityToDto(Album album){

        return new AlbumResponseDto( album.getId(),
                album.getUsername(),
                album.getTitle(),
                album.getArtist(),
                album.getAge(),
                album.isAlbumprivate(),
                album.getImageHashCode()
        );
    }


}
