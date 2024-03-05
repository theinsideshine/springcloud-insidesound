package com.theinsideshine.insidesound.backend.tracks.models.dto;

import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.utils.validations.MaxFileSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record TrackRequestDto (

    Long id,

    @NotBlank
    @Size(min = 4, max = 8)
    String username,

    @NotBlank
    @Size(min = 4, max = 30)
    String title,

    @MaxFileSize(value = 1048576, message = "imageFile")
    MultipartFile imageFile,

    @MaxFileSize(value = 10485760, message = "mp3File")
    MultipartFile mp3File,

    Long album_id
){
    public static Track TrackRequestDtoMapperDtoToEntity(TrackRequestDto trackRequestDto)  {
        try {
            byte[] imageBytes = trackRequestDto.imageFile().getBytes();
            byte[] mp3Bytes   = trackRequestDto.mp3File().getBytes();
            return new Track(
                    trackRequestDto.id,
                    trackRequestDto.username,
                    trackRequestDto.title,
                    imageBytes,
                    mp3Bytes,
                    trackRequestDto.album_id
            );
        } catch (Exception e) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_CONV_MULTIPARTFILE);
        }
    }
}


