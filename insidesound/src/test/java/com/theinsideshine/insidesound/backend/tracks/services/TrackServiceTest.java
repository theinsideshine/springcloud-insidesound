package com.theinsideshine.insidesound.backend.tracks.services;

import com.theinsideshine.insidesound.backend.FakeImageProvider;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.datas.TrackData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.theinsideshine.insidesound.backend.datas.AlbumData.getFakeImageBytes;
import static com.theinsideshine.insidesound.backend.datas.TrackData.getMultipartFileImage;
import static com.theinsideshine.insidesound.backend.datas.TrackData.getMultipartFileMp3;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@SpringBootTest
public class TrackServiceTest {
    private List<TrackResponseDto> tracksResponseDto;
    private TrackRequestDto trackRequestDto;
    private List<Track> tracks;
    @Autowired
    private TrackService trackService;
    @MockBean
    private AlbumRepository albumRepository;
    @MockBean
    private TrackRepository trackRepository;

    /*
        Aca usamos TrackData para poder probar la generacion del hashcode
     */
    @BeforeEach
    public void setup() {
        tracks = Arrays.asList(
                new Track(1L, "German", "Abre", getFakeImageBytes(), getFakeImageBytes(), 4L),
                new Track(1L, "Sofia", "Luna de marzo", getFakeImageBytes(), getFakeImageBytes(), 5L)
        );
        tracksResponseDto = tracks.stream()
                .map(TrackResponseDto::trackResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
        MockMultipartFile multipartFileImage = getMultipartFileImage();
        MockMultipartFile multipartFileMp3 = getMultipartFileMp3();
        trackRequestDto =
                new TrackRequestDto(1L, "German", "Abre", multipartFileImage, multipartFileMp3, 6L);
    }

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del mock de trackRepository.findAll() para devolver las entidades de track
        given(trackRepository.findAll()).willReturn(tracks);
        // Llamar al método findAll() del servicio
        List<TrackResponseDto> result = trackService.findAll();
        // Verificar que el resultado devuelto coincide con los DTOs de respuesta de tracks originales
        assertEquals(tracksResponseDto.size(), result.size());
        for (int i = 0; i < tracksResponseDto.size(); i++) {
            assertEquals(tracksResponseDto.get(i), result.get(i));
        }
        // Verificar que el método albumRepository.findAll() fue invocado exactamente una vez
        verify(trackRepository, times(1)).findAll();
    }

    @Test
    public void testFindImageById_ImageFound() {
        // ID del álbum a buscar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track con el ID dado
        given(trackRepository.findById(id)).willReturn(Optional.of(tracks.get(0)));
        // Llamar al método findImageById() del servicio
        Resource result = trackService.findImageById(id);
        // Verificar que el resultado no sea nulo y que contenga los bytes de la imagen esperados
        assertNotNull(result);
        assertTrue(result instanceof ByteArrayResource);
        assertArrayEquals(FakeImageProvider.getFakeImageBytes(), ((ByteArrayResource) result).getByteArray());
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
    }

    @Test
    public void testFindImageById_ImageNotFound() {
        // ID del álbum a buscar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track vacío con el ID dado
        given(trackRepository.findById(id)).willReturn(Optional.empty());
        // Verificar que al llamar al método findImageById() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.findImageById(id));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga el segundo campo del Enum
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.IMG_NOT_FOUND_BY_TRACK_ID.getErrorMap().get("GET IMAGES")));
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
    }

    @Test
    public void testFindMp3ById_ImageFound() {
        // ID del álbum a buscar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track con el ID dado
        given(trackRepository.findById(id)).willReturn(Optional.of(tracks.get(0)));
        // Llamar al método findMp3ById() del servicio
        Resource result = trackService.findMp3ById(id);
        // Verificar que el resultado no sea nulo y que contenga los bytes del mp3 esperados
        assertNotNull(result);
        assertTrue(result instanceof ByteArrayResource);
        assertArrayEquals(FakeImageProvider.getFakeImageBytes(), ((ByteArrayResource) result).getByteArray());
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
    }

    @Test
    public void testFindImageById_ImageBytesNull() {
        // ID del álbum a buscar
        Long id = tracks.get(0).getId();
        tracks.get(0).setImage(null);
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track vacío con el ID dado
        given(trackRepository.findById(id)).willReturn(Optional.of(tracks.get(0)));
        // Verificar que al llamar al método findMp3ById() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.findImageById(id));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga el segundo campo del Enum
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.IMG_NOT_FOUND_BY_TRACK_ID.getValueMapErrorMessage()));
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
    }

    @Test
    public void testFindMp3ById_Mp3NotFound() {
        // ID del álbum a buscar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track vacío con el ID dado
        given(trackRepository.findById(id)).willReturn(Optional.empty());
        // Verificar que al llamar al método findMp3ById() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.findMp3ById(id));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga el segundo campo del Enum
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.MP3_NOT_FOUND_BY_TRACK_ID.getValueMapErrorMessage()));
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
    }

    @Test
    public void testFindMp3ById_Mp3BytesNull() {
        // ID del álbum a buscar
        Long id = tracks.get(0).getId();
        tracks.get(0).setMp3(null);
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track vacío con el ID dado
        given(trackRepository.findById(id)).willReturn(Optional.of(tracks.get(0)));
        // Verificar que al llamar al método findMp3ById() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.findMp3ById(id));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga el segundo campo del Enum
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.MP3_NOT_FOUND_BY_TRACK_ID.getValueMapErrorMessage()));
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByAlbumId_TracksFound() {
        // Nombre de usuario para buscar
        Long album_id = tracksResponseDto.get(0).album_id();
        // Configurar el comportamiento del mock de trackRepository.findByAlbumId() para devolver la lista de álbumes ficticia
        given(trackRepository.findByAlbumId(album_id)).willReturn(tracks);
        // Llamar al método findByAlbumId() del servicio
        List<TrackResponseDto> result = trackService.findByAlbumId(album_id);
        // Verificar que el resultado no sea nulo y que contenga los DTOs de respuesta de álbum correspondientes
        assertNotNull(result);
        assertEquals(tracks.size(), result.size());
        for (int i = 0; i < tracks.size(); i++) {
            assertEquals(tracks.get(i).getId(), result.get(i).id());
            assertEquals(tracks.get(i).getUsername(), result.get(i).username());
            assertEquals(tracks.get(i).getTitle(), result.get(i).title());
            assertEquals(tracks.get(i).getAlbum_id(), result.get(i).album_id());
        }
        // Verificar que el método trackRepository.findByAlbumId() fue invocado exactamente una vez con el nombre de id dado
        verify(trackRepository, times(1)).findByAlbumId(album_id);
    }

    @Test
    public void testFindByAlbum_Id_TrackNotFound() {
        // album_id para buscar
        Long album_id = tracksResponseDto.get(0).album_id();
        // Configurar el comportamiento del mock de trackRepository.findByAlbumId() para devolver una lista vacía
        given(trackRepository.findByAlbumId(album_id)).willReturn(Collections.emptyList());
        // Verificar que al llamar al método findByAlbumId() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.findByAlbumId(album_id));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga la descripción asociada en el enum para ALBUM_PUBLIC_NOT_FOUND_BY_USERNAME
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.TRACK_NOT_FOUND_BY_ALBUM_ID.getValueMapErrorMessage()));
        // Verificar que el método albumRepository.findByUsernameAndAlbumprivateFalse() fue invocado exactamente una vez con el nombre de usuario dado
        verify(trackRepository, times(1)).findByAlbumId(album_id);
    }

    @Test
    public void testGetAlbumIdByTrackId() {
        // trackId para buscar
        Long trackId = tracksResponseDto.get(0).id();
        // album_id esperado
        Long expectedAlbumId = tracksResponseDto.get(0).album_id();
        //Devuelve el track 0 que es el mismo que tracksResponseDto.get(0)
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(tracks.get(0)));
        //prueba trackService.getAlbumIdByTrackId
        Long realAlbumId = trackService.getAlbumIdByTrackId(trackId);
        assertEquals(expectedAlbumId, realAlbumId);
        // Caso en el que se encuentra el track pero su album_id es 0
        tracks.get(0).setAlbum_id(0L);
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(tracks.get(0)));
        // Prueba trackService.getAlbumIdByTrackId cuando se encuentra el track pero su album_id es 0
        Long realAlbumIdWhenAlbumIdIsZero = trackService.getAlbumIdByTrackId(trackId);
        assertNull(realAlbumIdWhenAlbumIdIsZero);
    }

    @Test
    public void testFindByUsername_TracksFound() {
        // Nombre de usuario para buscar
        String username = tracksResponseDto.get(0).username();
        // Configurar el comportamiento del mock de trackRepository.findByAlbumId() para devolver la lista de álbumes ficticia
        given(trackRepository.findByUsername(username)).willReturn(tracks);
        // Llamar al método findByAlbumId() del servicio
        List<TrackResponseDto> result = trackService.findByUsername(username);
        // Verificar que el resultado no sea nulo y que contenga los DTOs de respuesta de álbum correspondientes
        assertNotNull(result);
        assertEquals(tracks.size(), result.size());
        for (int i = 0; i < tracks.size(); i++) {
            assertEquals(tracks.get(i).getId(), result.get(i).id());
            assertEquals(tracks.get(i).getUsername(), result.get(i).username());
            assertEquals(tracks.get(i).getTitle(), result.get(i).title());
            assertEquals(tracks.get(i).getAlbum_id(), result.get(i).album_id());
        }
        // Verificar que el método trackRepository.findByAlbumId() fue invocado exactamente una vez con el nombre de id dado
        verify(trackRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testSave() {
        // Crear un álbum ficticio y un DTO de respuesta de álbum correspondiente
        Track savedTrack = tracks.get(0);
        TrackResponseDto expectedDto = tracksResponseDto.get(0);
        // Configurar el comportamiento del mock de trackRepository.save() para devolver el track guardado
        given(trackRepository.save(any(Track.class))).willReturn(savedTrack);
        // Llamar al método save() del servicio
        TrackResponseDto result = trackService.save(trackRequestDto);
        // Verificar que el resultado no sea nulo
        assertNotNull(result);
        // Verificar que el resultado devuelto sea igual al DTO de respuesta de álbum esperado
        assertEquals(expectedDto, result);
        // Verificar que el método albumRepository.save() fue invocado exactamente una vez con cualquier instancia de Album
        verify(trackRepository, times(1)).save(any(Track.class));
    }

    @Test
    public void testUpdate() {
        // ID del álbum a actualizar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver el track a actualizar
        Track trackToUpdate = tracks.get(0);
        given(trackRepository.findById(id)).willReturn(Optional.of(trackToUpdate));
        // Configurar el comportamiento del mock de trackRepository.save() para devolver el álbum actualizado
        //Album updatedAlbum = albums.get(0);
        given(trackRepository.save(any(Track.class))).willReturn(trackToUpdate);
        // Llamar al método update() del servicio
        TrackResponseDto result = trackService.update(trackRequestDto, id);
        // Verificar que el resultado no sea nulo
        assertNotNull(result);
        // Verificar que el título del álbum actualizado sea el esperado
        assertEquals(trackRequestDto.title(), result.title());
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
        // Verificar que el método trackRepository.save() fue invocado exactamente una vez con cualquier instancia de Album
        verify(trackRepository, times(1)).save(any(Track.class));
    }

    @Test
    public void testUpdate_ThrowsExceptions() {
        // ID del álbum a actualizar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver el track a actualizar
        Track trackToUpdate = tracks.get(0);
        given(trackRepository.findById(id)).willReturn(Optional.of(trackToUpdate));
        // Configurar el comportamiento del mock de trackRepository.save() para lanzar una excepción al ser llamado con cualquier instancia de Track
        when(trackRepository.save(any(Track.class))).thenThrow(new RuntimeException("Simulated save error"));
        // Llamar al método update() del servicio y verificar que se lance la excepción InsidesoundException
        assertThrows(InsidesoundException.class, () -> trackService.update(trackRequestDto, id));
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
        // Verificar que el método trackRepository.save() fue invocado exactamente una vez con cualquier instancia de Track
        verify(trackRepository, times(1)).save(any(Track.class));
    }

    @Test
    public void testUpdate_validateTrackIdPost() {
        // ID del álbum a actualizar
        Long id = tracks.get(0).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un Optional vacío
        given(trackRepository.findById(id)).willReturn(Optional.empty());
        // Llamar al método update() del servicio y verificar que se lance la excepción InsidesoundException
        assertThrows(InsidesoundException.class, () -> trackService.update(trackRequestDto, id));
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
        // Verificar que el método trackRepository.save() no fue invocado debido a que no se encontró ningún track
        verify(trackRepository, never()).save(any(Track.class));
    }

    @Test
    public void testRemove_IsPresent() {
        // Caso 1: Track encontrado con el ID dado
        Long idExistente = tracks.get(1).getId();
        Track existingTrack = tracks.get(1);
        given(trackRepository.findById(idExistente)).willReturn(Optional.of(existingTrack));
        // Llamar al método remove() del servicio y verificar que no se lance ninguna excepción
        trackService.remove(idExistente);
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(idExistente);
        // Verificar que el método albumRepository.deleteById() fue invocado exactamente una vez con el ID del track existente
        verify(trackRepository, times(1)).deleteById(idExistente);
    }

    @Test
    public void testRemove_IdNotFound() {
        // ID de un track que no existe
        Long nonExistentId = 999L;
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un Optional vacío
        when(trackRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        // Llamar al método remove() del servicio
        trackService.remove(nonExistentId);
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(nonExistentId);
        // Verificar que el método trackRepository.deleteById() no fue invocado
        verify(trackRepository, never()).deleteById(any());
    }

    @Test
    public void testRemove_ThrowsException() {
        // ID del álbum a eliminar
        Long id = tracks.get(1).getId();
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un track existente
        Track existingTrack = tracks.get(1);
        when(trackRepository.findById(id)).thenReturn(Optional.of(existingTrack));
        // Configurar el comportamiento del mock de trackRepository.deleteById() para lanzar una excepción al ser llamado con el ID dado
        doThrow(new RuntimeException("Simulated delete error")).when(trackRepository).deleteById(id);
        // Llamar al método remove() del servicio
        assertThrows(InsidesoundException.class, () -> trackService.remove(id));
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(trackRepository, times(1)).findById(id);
        // Verificar que el método albumRepository.deleteById() fue invocado exactamente una vez con el ID del álbum existente
        verify(trackRepository, times(1)).deleteById(id);
    }

    @Test
    public void testAssociateAlbumToTrack() {
        // IDs de la pista y del álbum
        Long trackId = 1L;
        Long albumId = 10L;
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un Track existente
        Track track = new Track();
        track.setId(trackId);
        given(trackRepository.findById(trackId)).willReturn(Optional.of(track));
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un Album existente
        Album album = new Album();
        album.setId(albumId);
        given(albumRepository.findById(albumId)).willReturn(Optional.of(album));
        // Configurar el comportamiento del mock de trackRepository.save() para no lanzar ninguna excepción
        given(trackRepository.save(any(Track.class))).willReturn(track);
        // Llamar al método associateAlbumToTrack() del servicio
        assertDoesNotThrow(() -> trackService.associateAlbumToTrack(trackId, albumId));
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID de la pista
        verify(trackRepository, times(1)).findById(trackId);
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID del álbum
        verify(albumRepository, times(1)).findById(albumId);
        // Verificar que el método trackRepository.save() fue invocado exactamente una vez con el objeto Track asociado al álbum
        verify(trackRepository, times(1)).save(track);
        // Verificar que la pista se asoció correctamente al álbum
        assertEquals(albumId, track.getAlbum_id());
    }

    @Test
    public void testAssociateAlbumToTrack_ExceptionHandling() {
        // IDs de la pista y del álbum
        Long trackId = 1L;
        Long albumId = 1L;
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un Track existente
        Track track = new Track();
        track.setId(trackId);
        given(trackRepository.findById(trackId)).willReturn(Optional.of(track));
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un Album existente
        Album album = new Album();
        album.setId(albumId);
        given(albumRepository.findById(albumId)).willReturn(Optional.of(album));
        // Configurar el comportamiento del mock de trackRepository.save() para lanzar una excepción
        given(trackRepository.save(any(Track.class))).willThrow(new RuntimeException("Simulated save error"));
        // Llamar al método associateAlbumToTrack() del servicio y verificar que lance la excepción InsidesoundException
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.associateAlbumToTrack(trackId, albumId));
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID de la pista
        verify(trackRepository, times(1)).findById(trackId);
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID del álbum
        verify(albumRepository, times(1)).findById(albumId);
        // Verificar que el método trackRepository.save() fue invocado exactamente una vez con el objeto Track asociado al álbum
        verify(trackRepository, times(1)).save(track);
        // Verificar que se lanzó la excepción con el código de error esperado
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.ERR_UPDATING_TRACK.getValueMapErrorMessage()));
    }

    @Test
    public void testAssociateAlbumToTrack_ExceptionTRACKIDNOTFOUND() {
        // IDs de la track
        Long trackId = 1L;
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un Album existente
        Album album = AlbumData.getAlbum();
        given(albumRepository.findById(album.getId())).willReturn(Optional.of(album));
        // Llamar al método associateAlbumToTrack() del servicio y verificar que lance la excepción InsidesoundException con el código correcto
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.associateAlbumToTrack(trackId, album.getId()));
        // Verificar que se lance la excepción con el código de error esperado
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.TRACK_ID_NOT_FOUND.getValueMapErrorMessage()));
    }

    @Test
    public void testAssociateAlbumToTrack_ExceptionALBUMIDNOTFOUND() {
        // IDs de la track
        Long albumId = 1L;
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un Album existente
        Track track = TrackData.getTrack();
        given(trackRepository.findById(track.getId())).willReturn(Optional.of(track));
        // Llamar al método associateAlbumToTrack() del servicio y verificar que lance la excepción InsidesoundException con el código correcto
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> trackService.associateAlbumToTrack(track.getId(), albumId));
        // Verificar que se lance la excepción con el código de error esperado
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.ALBUM_ID_NOT_FOUND.getValueMapErrorMessage()));
    }
}
