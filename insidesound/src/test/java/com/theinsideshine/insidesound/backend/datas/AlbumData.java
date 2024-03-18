package com.theinsideshine.insidesound.backend.datas;

import com.theinsideshine.insidesound.backend.FakeImageProvider;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumData {


    public static List<Album> getAlbumsList(){

        List<Album> albums = Arrays.asList(
                new Album(1L, "German", "Abre", "Fito Paez", "2022", true, getFakeImageBytes()),
                new Album(2L, "Sol", "Mint", "EL cuervo", "2001", false, getFakeImageBytes())
        );
        return albums;
    }
    public static Album getAlbum() {
        Album album = new Album(1L, "Yuya", "Los bajitos", "XUXA", "1022", false, getFakeImageBytes());
        return album;
    }

    public static List<AlbumResponseDto> getAlbumsResponseDtoList(){

        List<Album> albums = getAlbumsList();
        List<AlbumResponseDto>  albumsResponseDto = albums.stream()
                .map(AlbumResponseDto::albumResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());

         return albumsResponseDto;
    }




    public static AlbumResponseDto getAlbumResponseDto(){

        AlbumResponseDto albumResponseDto =
                new AlbumResponseDto(1L, "German", "Abre", "Fito Paez", "2022", true, null);

        return albumResponseDto;
    }
    public static MockMultipartFile getMultipartFile() {

        MockMultipartFile multipartFile = new MockMultipartFile(
                "imageFile",                // Nombre del parámetro del archivo
                "image.jpg",                // Nombre del archivo
                MediaType.IMAGE_JPEG_VALUE, // Tipo de contenido de la imagen
                getFakeImageBytes()              // Bytes de la imagen
        );
        return multipartFile;
    }
    public static byte[] getFakeImageBytes(){
        return FakeImageProvider.getFakeImageBytes();
    }
    public static AlbumRequestDto getAlbumRequestDto() {
        MockMultipartFile multipartFile = getMultipartFile();
        AlbumRequestDto albumRequestDto = new AlbumRequestDto(1L, "Yuya", "Los bajitos", "XUXA", "1022", false, multipartFile);
        return albumRequestDto;
    }


}
