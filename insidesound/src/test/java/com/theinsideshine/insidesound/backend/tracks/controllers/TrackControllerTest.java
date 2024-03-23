package com.theinsideshine.insidesound.backend.tracks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theinsideshine.insidesound.backend.FakeImageProvider;
import com.theinsideshine.insidesound.backend.datas.TrackData;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.services.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class TrackControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TrackService trackService;
    @InjectMocks
    private TrackController trackController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(trackController)
                .build();
    }

    @Test
    public void testShowList() throws Exception {
        // Simulamos que el servicio devuelve la lista de álbumes
        List<TrackResponseDto> trackResponseDtoList = TrackData.getTracksResponseDtoList();
        given(trackService.findAll()).willReturn(trackResponseDtoList);
        // Realiza una solicitud GET a la ruta "/tracks/list"
        mockMvc.perform(get("/tracks"))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica que el cuerpo de la respuesta contenga la lista de álbumes esperada
                .andExpect(content().json(objectMapper.writeValueAsString(trackResponseDtoList)));
        verify(trackService, times(1)).findAll();
    }

    @Test
    public void testShowImageByTrackId() throws Exception {
        // ID válido para un track existente
        Long validId = 1L;
        // Simulamos que el track con el ID válido tiene una imagen asociada
        Resource fakeImageResource = new ByteArrayResource(FakeImageProvider.getFakeImageBytes());
        given(trackService.findImageById(validId)).willReturn(fakeImageResource);
        // Realiza una solicitud GET a la ruta "/tracks/img/{id}" con el ID válido
        mockMvc.perform(get("/tracks/img/{id}", validId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea una imagen JPEG
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                // Verifica que el cuerpo de la respuesta contenga la imagen ficticia
                .andExpect(content().bytes(FakeImageProvider.getFakeImageBytes()));
        verify(trackService, times(1)).findImageById(validId);
    }

    @Test
    public void testShowMp3ByTrackId() throws Exception {
        // ID válido para un track existente
        Long validId = 1L;
        // Simulamos que el track con el ID válido tiene una imagen asociada
        Resource fakeImageResource = new ByteArrayResource(FakeImageProvider.getFakeImageBytes());
        given(trackService.findMp3ById(validId)).willReturn(fakeImageResource);
        // Realiza una solicitud GET a la ruta "/tracks/mp3/{id}" con el ID válido
        mockMvc.perform(get("/tracks/mp3/{id}", validId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea una imagen JPEG
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                // Verifica que el cuerpo de la respuesta contenga la imagen ficticia
                .andExpect(content().bytes(FakeImageProvider.getFakeImageBytes()));
        verify(trackService, times(1)).findMp3ById(validId);
    }

    @Test
    public void testShowTracksByUsername() throws Exception {
        // Nombre de usuario válido con tracks asociados
        List<TrackResponseDto> trackResponseDtoList = TrackData.getTracksResponseDtoList();
        String validUsername = trackResponseDtoList.get(0).username();
        // Simulamos que el nombre de usuario tiene tracks asociados
        given(trackService.findByUsername(validUsername)).willReturn(trackResponseDtoList);
        // Realiza una solicitud GET a la ruta "/tracks/by-username/{username}" con el nombre de usuario válido
        mockMvc.perform(get("/tracks/by-username/{username}", validUsername))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica que el cuerpo de la respuesta contenga la lista de tracks esperada
                .andExpect(content().json(objectMapper.writeValueAsString(trackResponseDtoList)));
        verify(trackService, times(1)).findByUsername(validUsername);
    }

    @Test
    public void testShowTrackByAlbumId() throws Exception {
        // AlbumID válido para un track existente
        List<TrackResponseDto> trackResponseDtoList = TrackData.getTracksResponseDtoList();
        Long validAlbumId = trackResponseDtoList.get(0).album_id();
        // Simulamos que el track con el AlbumId válido tiene una lista TrackResponseDto  asociada
        given(trackService.findByAlbumId(validAlbumId)).willReturn(trackResponseDtoList);
        // Realiza una solicitud GET a la ruta "/tracks/img/{id}" con el ID válido
        mockMvc.perform(get("/tracks/by-album-id/{id}", validAlbumId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk());
        verify(trackService, times(1)).findByAlbumId(validAlbumId);
    }

    @Test
    public void testGetAlbumIdByTrackId() throws Exception {
        // AlbumID válido para un trackId existente
        TrackResponseDto trackResponseDto = TrackData.getTrackResponseDto();
        Long validTrackId = trackResponseDto.id();
        Long responseAlbumId = trackResponseDto.album_id();
        // Simulamos que el track con el TrackId válido para que devuelva albumId
        given(trackService.getAlbumIdByTrackId(validTrackId)).willReturn(responseAlbumId);
        // Realiza una solicitud GET a la ruta "/tracks/img/{id}" con el ID válido
        mockMvc.perform(get("/tracks/{trackId}/album", validTrackId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk());
        // Simulamos que el track con el TrackId válido para que devuelva null
        given(trackService.getAlbumIdByTrackId(validTrackId)).willReturn(null);
        // Realiza una solicitud GET a la ruta "/tracks/img/{id}" con el ID válido
        mockMvc.perform(get("/tracks/{trackId}/album", validTrackId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk());
        verify(trackService, times(2)).getAlbumIdByTrackId(validTrackId);
    }

    @Test
    public void testCreateTrack() throws Exception {
        MockMultipartFile multipartFileImage = TrackData.getMultipartFileImage();
        MockMultipartFile multipartFileMp3 = TrackData.getMultipartFileMp3();
        TrackResponseDto trackResponseDto = TrackData.getTrackResponseDto();
        // Configurar el comportamiento esperado del servicio para el método create
        when(trackService.save(any(TrackRequestDto.class))).thenReturn(trackResponseDto);
        // Realizar una solicitud POST a la ruta "/tracks" con los MockMultipartFile adjuntos
        mockMvc.perform(MockMvcRequestBuilders.multipart("/tracks")
                        .file(multipartFileImage)
                        .file(multipartFileMp3)
                        .param("username", "German")
                        .param("title", "Abre")
                        .param("album_id", "0")
                        .with(request -> {
                            request.setMethod("POST"); // Establecer el método HTTP como POST
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTrack() throws Exception {
        MockMultipartFile multipartFileImage = TrackData.getMultipartFileImage();
        MockMultipartFile multipartFileMp3 = TrackData.getMultipartFileMp3();
        TrackResponseDto trackResponseDto = TrackData.getTrackResponseDto();
        // Cuando se llame al método update del servicio, devuelve un objeto AlbumResponseDto
        when(trackService.update(any(TrackRequestDto.class), any(Long.class)))
                .thenReturn(trackResponseDto);
        // Realizar una solicitud PUT a la ruta "/tracks/{id}" con el objeto trackRequestDto y el ID correspondiente
        mockMvc.perform(MockMvcRequestBuilders.multipart("/tracks/{id}", 1L)
                        .file(multipartFileImage)
                        .file(multipartFileMp3)
                        .param("username", "German")
                        .param("title", "Abre")
                        .param("album_id", "0")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveTrack() throws Exception {
        Long trackId = 1L;
        // Realizar una solicitud DELETE a la ruta "/tracks/{id}"
        mockMvc.perform(delete("/tracks/{id}", trackId))
                .andExpect(status().isOk());
        // Verificar que el método remove del servicio haya sido invocado con el ID correcto
        verify(trackService, times(1)).remove(trackId);
    }

    @Test
    public void testAssociateAlbumToTrack_Success() throws Exception {
        // Configurar el comportamiento del servicio mock para el caso feliz
        doNothing().when(trackService).associateAlbumToTrack(anyLong(), anyLong());

        // Realizar la solicitud POST a la ruta correspondiente con los parámetros adecuados
        mockMvc.perform(MockMvcRequestBuilders.post("/tracks/{trackId}/associateAlbum", 1L)
                        .param("albumId", "1"))
                .andExpect(status().isOk());// Se espera una respuesta exitosa con código 200

    }


}
