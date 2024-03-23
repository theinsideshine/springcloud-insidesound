package com.theinsideshine.insidesound.backend.albums.models.dtos;

import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AlbumRequestDtoTest {


    @Mock
    private MockMultipartFile mockImageFile;

    @Test
    public void testAlbumRequestDtoMapperDtoToEntity_ConversionError() throws IOException {
        // Configurar el mock para lanzar IOException al obtener bytes
        when(mockImageFile.getBytes()).thenThrow(IOException.class);
        // Crear un objeto TrackRequestDto con datos de prueba
        AlbumRequestDto albumRequestDto = new AlbumRequestDto(
                1L,
                "username",
                "title",
                "artist",
                "ages",
                true,
                mockImageFile
        );
        // Llamar al método que se va a probar y esperar que lance la excepción
        assertThrows(InsidesoundException.class, () -> AlbumRequestDto.AlbumRequestDtoMapperDtoToEntity(albumRequestDto));
    }

    @Test
    void testAlbumRequestDtoMapperDtoToEntity() throws IOException {
        AlbumRequestDto expectedAlbumRequestDto = AlbumData.getAlbumRequestDto();
        Album realAlbum = AlbumRequestDto.AlbumRequestDtoMapperDtoToEntity(expectedAlbumRequestDto);
        assertEquals(expectedAlbumRequestDto.id(), realAlbum.getId());
        assertEquals(expectedAlbumRequestDto.username(), realAlbum.getUsername());
        assertEquals(expectedAlbumRequestDto.title(), realAlbum.getTitle());
        assertEquals(expectedAlbumRequestDto.artist(), realAlbum.getArtist());
        assertEquals(expectedAlbumRequestDto.age(), realAlbum.getAge());
        assertEquals(expectedAlbumRequestDto.albumprivate(), realAlbum.isAlbumprivate());
        assertEquals(expectedAlbumRequestDto.imageFile().getBytes(), realAlbum.getImage());
    }

}
