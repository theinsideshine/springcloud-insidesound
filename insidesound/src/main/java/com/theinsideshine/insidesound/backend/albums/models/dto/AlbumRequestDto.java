package com.theinsideshine.insidesound.backend.albums.models.dto;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.utils.validations.MaxFileSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record AlbumRequestDto(

    Long id,
    @NotBlank
    @Size(min = 4, max = 8)
    String username,

    @NotBlank
    @Size(min = 4, max = 20)
    String title,

    @NotBlank
    @Size(min = 4, max = 23)
    String artist,
    @Size(min = 3, max = 4)
    String age,

    boolean albumprivate,

    @MaxFileSize(value = 1048576, message = "imageFile")
    MultipartFile imageFile
){
    public static Album AlbumRequestDtoMapperDtoToEntity(AlbumRequestDto albumRequestDto)  {
        try {
            byte[] imageBytes = albumRequestDto.imageFile().getBytes();
            return new Album(
                    albumRequestDto.id,
                    albumRequestDto.username,
                    albumRequestDto.title,
                    albumRequestDto.artist,
                    albumRequestDto.age,
                    albumRequestDto.albumprivate,
                    imageBytes
            );
        } catch (Exception e) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_CONV_IMAGE);
        }
    }
}

