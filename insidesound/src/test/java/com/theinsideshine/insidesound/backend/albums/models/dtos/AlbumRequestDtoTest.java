package com.theinsideshine.insidesound.backend.albums.models.dtos;

import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AlbumRequestDtoTest {

    @Test
    void testAlbumRequestDtoMapperDtoToEntity() throws IOException {
        AlbumRequestDto expectedAlbumRequestDto = AlbumData.getAlbumRequestDto();
        Album realAlbum= AlbumRequestDto.AlbumRequestDtoMapperDtoToEntity(expectedAlbumRequestDto);
        assertEquals(expectedAlbumRequestDto.id(), realAlbum.getId());
        assertEquals(expectedAlbumRequestDto.username(), realAlbum.getUsername());
        assertEquals(expectedAlbumRequestDto.title(), realAlbum.getTitle());
        assertEquals(expectedAlbumRequestDto.artist(), realAlbum.getArtist());
        assertEquals(expectedAlbumRequestDto.age(), realAlbum.getAge());
        assertEquals(expectedAlbumRequestDto.albumprivate(), realAlbum.isAlbumprivate());
        assertEquals(expectedAlbumRequestDto.imageFile().getBytes(), realAlbum.getImage());
    }

}
