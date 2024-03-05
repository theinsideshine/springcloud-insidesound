package com.theinsideshine.insidesound.backend.tracks.models.dto;

import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;

public record TrackResponseDto(
         Long id,
         String username,
         String title,
         Integer image,
         Integer mp3,
         Long album_id

) {
 public static TrackResponseDto trackResponseDtoMapperEntityToDto(Track track){

        return new TrackResponseDto( track.getId(),
                track.getUsername(),
                track.getTitle(),
                track.getImageHashCode(),
                track.getMp3HashCode(),
                track.getAlbum_id()
        );
    }


}
