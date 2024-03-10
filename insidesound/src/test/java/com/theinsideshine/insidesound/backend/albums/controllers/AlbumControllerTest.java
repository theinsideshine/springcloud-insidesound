package com.theinsideshine.insidesound.backend.albums.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.services.AlbumService;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AlbumControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AlbumService albumService;

    @InjectMocks
    private AlbumController albumController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<AlbumResponseDto> albumsResponseDto;

    private AlbumRequestDto albumRequestDto;

    private MockMultipartFile multipartFile;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(albumController)
                .build();

        // Datos de ejemplo para el test
        albumsResponseDto = Arrays.asList(
                new AlbumResponseDto(1L, "German", "Abre", "Fito Paez", "2022", true, null),
                new AlbumResponseDto(2L, "Sol", "Mint", "EL cuervo", "2001", false, null)
        );

        // Obtener los bytes de la imagen ficticia utilizando el método de FakeImageProvider
        byte[] fakeImageBytes = FakeImageProvider.getFakeImageBytes();

        // Crear un objeto MockMultipartFile con los bytes de la imagen
        multipartFile = new MockMultipartFile(
                "imageFile",                // Nombre del parámetro del archivo
                "image.jpg",                // Nombre del archivo
                MediaType.IMAGE_JPEG_VALUE, // Tipo de contenido de la imagen
                fakeImageBytes              // Bytes de la imagen
        );

        albumRequestDto = new AlbumRequestDto(1L, "Yuya", "Los bajitos", "XUXA", "1022", false, multipartFile);
    }


    @Test
    public void testShowList() throws Exception {

        // Simulamos que el servicio devuelve la lista de álbumes
        given(albumService.findAll()).willReturn(albumsResponseDto);

        // Realiza una solicitud GET a la ruta "/albums/list"
        mockMvc.perform(get("/albums"))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica que el cuerpo de la respuesta contenga la lista de álbumes esperada
                .andExpect(content().json(objectMapper.writeValueAsString(albumsResponseDto)));
    }

    @Test
    public void testShowAlbumById() throws Exception {
        // ID válido para un álbum existente
        Long validId = albumsResponseDto.get(0).id();
        // Simulamos que el álbum con el ID válido existe

        given(albumService.findById(validId)).willReturn(Optional.of(albumsResponseDto.get(0)));

        // Realiza una solicitud GET a la ruta "/albums/{id}" con el ID válido
        mockMvc.perform(get("/albums/{id}", validId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica que el cuerpo de la respuesta contenga el álbum esperado
                .andExpect(content().json(objectMapper.writeValueAsString(albumsResponseDto.get(0))));
    }

    @Test
    public void testShowImageByAlbumId() throws Exception {
        // ID válido para un álbum existente
        Long validId = 1L;
        // Bytes ficticios de una imagen JPEG
        byte[] fakeImageBytes = FakeImageProvider.getFakeImageBytes();
        // Simulamos que el álbum con el ID válido tiene una imagen asociada
        Resource fakeImageResource = new ByteArrayResource(FakeImageProvider.getFakeImageBytes());
        given(albumService.findImageById(validId)).willReturn(fakeImageResource);

        // Realiza una solicitud GET a la ruta "/albums/img/{id}" con el ID válido
        mockMvc.perform(get("/albums/img/{id}", validId))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea una imagen JPEG
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                // Verifica que el cuerpo de la respuesta contenga la imagen ficticia
                .andExpect(content().bytes(FakeImageProvider.getFakeImageBytes()));

    }

    @Test
    public void testShowAlbumsByUsername() throws Exception {
        // Nombre de usuario válido con álbumes asociados
        String validUsername = albumsResponseDto.get(0).username();

        // Simulamos que el nombre de usuario tiene álbumes asociados
        given(albumService.findByUsername(validUsername)).willReturn(albumsResponseDto);
// Realiza una solicitud GET a la ruta "/albums/by-username/{username}" con el nombre de usuario válido
        mockMvc.perform(get("/albums/by-username/{username}", validUsername))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica que el cuerpo de la respuesta contenga la lista de álbumes esperada
                .andExpect(content().json(objectMapper.writeValueAsString(albumsResponseDto)));

    }

    @Test
    public void getPublicAlbumsByUsername() throws Exception {
        // Nombre de usuario válido con álbumes asociados
        String validUsername = albumsResponseDto.get(0).username();

        // Simulamos que el nombre de usuario tiene álbumes asociados
        given(albumService.findPublicAlbumsByUsername(validUsername)).willReturn(albumsResponseDto);
// Realiza una solicitud GET a la ruta "/public/by-username/{username}" con el nombre de usuario válido
        mockMvc.perform(get("/albums/public/by-username/{username}", validUsername))
                // Verifica que la respuesta tenga un estado OK (200)
                .andExpect(status().isOk())
                // Verifica que el tipo de contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verifica que el cuerpo de la respuesta contenga la lista de álbumes esperada
                .andExpect(content().json(objectMapper.writeValueAsString(albumsResponseDto)));

    }


    @Test
    public void testCreateAlbum() throws Exception {
        // Realizar una solicitud POST a la ruta "/albums" con el MockMultipartFile adjunto
        mockMvc.perform(MockMvcRequestBuilders.multipart("/albums")
                        .file(multipartFile)
                        .param("id", "1")
                        .param("username", "Yuya")
                        .param("title", "Los bajitos")
                        .param("artist", "XUXA")
                        .param("age", "1022")
                        .param("albumprivate", "false"))
                .andExpect(status().isOk());


    }
}
