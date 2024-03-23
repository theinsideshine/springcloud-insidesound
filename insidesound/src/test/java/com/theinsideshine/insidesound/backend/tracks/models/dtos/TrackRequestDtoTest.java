package com.theinsideshine.insidesound.backend.tracks.models.dtos;

import com.theinsideshine.insidesound.backend.datas.TrackData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
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
public class TrackRequestDtoTest {


    @Mock
    private MockMultipartFile mockImageFile;

    @Mock
    private MockMultipartFile mockMp3File;

    @Test
    public void testTrackRequestDtoMapperDtoToEntity_ConversionError() throws IOException {
        // Configurar el mock para lanzar IOException al obtener bytes
        when(mockMp3File.getBytes()).thenThrow(IOException.class);
        // Crear un objeto TrackRequestDto con datos de prueba
        TrackRequestDto trackRequestDto = new TrackRequestDto(
                1L,
                "username",
                "title",
                mockImageFile,
                mockMp3File,
                2L
        );
        // Llamar al método que se va a probar y esperar que lance la excepción
        assertThrows(InsidesoundException.class, () -> TrackRequestDto.TrackRequestDtoMapperDtoToEntity(trackRequestDto));
    }

    @Test
    void testTrackRequestDtoMapperDtoToEntity() throws IOException {
        TrackRequestDto expectedTrackRequestDto = TrackData.getTrackRequestDto();
        Track realTrack = TrackRequestDto.TrackRequestDtoMapperDtoToEntity(expectedTrackRequestDto);
        assertEquals(expectedTrackRequestDto.id(), realTrack.getId());
        assertEquals(expectedTrackRequestDto.username(), realTrack.getUsername());
        assertEquals(expectedTrackRequestDto.title(), realTrack.getTitle());
        assertEquals(expectedTrackRequestDto.imageFile().getBytes(), realTrack.getImage());
        assertEquals(expectedTrackRequestDto.mp3File().getBytes(), realTrack.getMp3());
        assertEquals(expectedTrackRequestDto.album_id(), realTrack.getAlbum_id());
    }


}
