package com.theinsideshine.insidesound.backend.albums.services;

import com.theinsideshine.insidesound.backend.FakeImageProvider;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@SpringBootTest
public class AlbumServiceTest {
    private List<AlbumResponseDto> albumsResponseDto;
    private AlbumRequestDto albumRequestDto;
    private List<Album> albums;
    @Autowired
    private AlbumService albumService;
    @MockBean
    private AlbumRepository albumRepository;
    @MockBean
    private TrackRepository trackRepository;
    /*
        Aca usamos AlbumData para poder probar la generacion del hashcode
     */
    @BeforeEach
    public void setup() {
        albums = Arrays.asList(
                new Album(1L, "German", "Abre", "Fito Paez", "2022", true, FakeImageProvider.getFakeImageBytes()),
                new Album(2L, "Sol", "Mint", "EL cuervo", "2001", false, FakeImageProvider.getFakeImageBytes())
        );
        albumsResponseDto = albums.stream()
                .map(AlbumResponseDto::albumResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
        albumRequestDto = new AlbumRequestDto(1L, "Germancito", "Abre", "Fito Paez", "2022", true, AlbumData.getMultipartFile());
    }

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del mock de albumRepository.findAll() para devolver las entidades de álbum
        given(albumRepository.findAll()).willReturn(albums);
        // Llamar al método findAll() del servicio
        List<AlbumResponseDto> result = albumService.findAll();
        // Verificar que el resultado devuelto coincide con los DTOs de respuesta de álbum originales
        assertEquals(albumsResponseDto.size(), result.size());
        for (int i = 0; i < albumsResponseDto.size(); i++) {
            assertEquals(albumsResponseDto.get(i), result.get(i));
        }
        // Verificar que el método albumRepository.findAll() fue invocado exactamente una vez
        verify(albumRepository, times(1)).findAll();
    }
    @Test
    public void testFindById() {
        // ID del álbum a buscar
        Long id = 1L;
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un álbum con el ID dado
        given(albumRepository.findById(id)).willReturn(Optional.of(albums.get(0)));
        // Llamar al método findById() del servicio
        Optional<AlbumResponseDto> result = albumService.findById(id);
        // Verificar que el resultado no sea nulo y que contenga el DTO de respuesta de álbum correspondiente
        assertTrue(result.isPresent());
        assertEquals(albumsResponseDto.get(0), result.get());
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(albumRepository, times(1)).findById(id);
    }
    @Test
    public void testFindImageById_ImageFound() {
        // ID del álbum a buscar
        Long id = albums.get(0).getId();
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un álbum con el ID dado
        given(albumRepository.findById(id)).willReturn(Optional.of(albums.get(0)));
        // Llamar al método findImageById() del servicio
        Resource result = albumService.findImageById(id);
        // Verificar que el resultado no sea nulo y que contenga los bytes de la imagen esperados
        assertNotNull(result);
        assertTrue(result instanceof ByteArrayResource);
        assertArrayEquals(FakeImageProvider.getFakeImageBytes(), ((ByteArrayResource) result).getByteArray());
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(albumRepository, times(1)).findById(id);
    }
    @Test
    public void testFindImageById_ImageNotFound() {
        // ID del álbum a buscar
        Long id = albums.get(0).getId();
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un álbum vacío con el ID dado
        given(albumRepository.findById(id)).willReturn(Optional.empty());
        // Verificar que al llamar al método findImageById() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> albumService.findImageById(id));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga el segundo campo del Enum
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.IMG_NOT_FOUND_BY_ALBUM_ID.getErrorMap().get("GET IMAGES")));
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(albumRepository, times(1)).findById(id);
    }
    @Test
    public void testFindPublicAlbumsByUsername_AlbumsFound() {
        // Nombre de usuario para buscar
        String username = "exampleUsername";
        // Configurar el comportamiento del mock de albumRepository.findByUsernameAndAlbumprivateFalse() para devolver la lista de álbumes ficticia
        given(albumRepository.findByUsernameAndAlbumprivateFalse(username)).willReturn(albums);
        // Llamar al método findPublicAlbumsByUsername() del servicio
        List<AlbumResponseDto> result = albumService.findPublicAlbumsByUsername(username);
        // Verificar que el resultado no sea nulo y que contenga los DTOs de respuesta de álbum correspondientes
        assertNotNull(result);
        assertEquals(albums.size(), result.size());
        for (int i = 0; i < albums.size(); i++) {
            assertEquals(albums.get(i).getId(), result.get(i).id());
            assertEquals(albums.get(i).getUsername(), result.get(i).username());
            assertEquals(albums.get(i).getTitle(), result.get(i).title());
            assertEquals(albums.get(i).getArtist(), result.get(i).artist());
            assertEquals(albums.get(i).getAge(), result.get(i).age());
            assertEquals(albums.get(i).isAlbumprivate(), result.get(i).albumprivate());
        }
        // Verificar que el método albumRepository.findByUsernameAndAlbumprivateFalse() fue invocado exactamente una vez con el nombre de usuario dado
        verify(albumRepository, times(1)).findByUsernameAndAlbumprivateFalse(username);
    }

    @Test
    public void testFindPublicAlbumsByUsername_AlbumsNotFound() {
        // Nombre de usuario para buscar
        String username = "nonExistingUser";
        // Configurar el comportamiento del mock de albumRepository.findByUsernameAndAlbumprivateFalse() para devolver una lista vacía
        given(albumRepository.findByUsernameAndAlbumprivateFalse(username)).willReturn(Collections.emptyList());
        // Verificar que al llamar al método findPublicAlbumsByUsername() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> albumService.findPublicAlbumsByUsername(username));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga la descripción asociada en el enum para ALBUM_PUBLIC_NOT_FOUND_BY_USERNAME
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.ALBUM_PUBLIC_NOT_FOUND_BY_USERNAME.getErrorMap().get("GET ALBUMS BY USERNAME")));
        // Verificar que el método albumRepository.findByUsernameAndAlbumprivateFalse() fue invocado exactamente una vez con el nombre de usuario dado
        verify(albumRepository, times(1)).findByUsernameAndAlbumprivateFalse(username);
    }


    @Test
    public void testFindByUsername_AlbumsFound() {
        // Nombre de usuario para buscar
        String username = "exampleUsername";
        // Configurar el comportamiento del mock de albumRepository.findByUsername() para devolver la lista de álbumes ficticia
        given(albumRepository.findByUsername(username)).willReturn(albums);
        // Llamar al método findByUsername() del servicio
        List<AlbumResponseDto> result = albumService.findByUsername(username);
        // Verificar que el resultado no sea nulo y que contenga los DTOs de respuesta de álbum correspondientes
        assertNotNull(result);
        assertEquals(albums.size(), result.size());
        for (int i = 0; i < albums.size(); i++) {
            assertEquals(albums.get(i).getId(), result.get(i).id());
            assertEquals(albums.get(i).getTitle(), result.get(i).title());
            assertEquals(albums.get(i).getTitle(), result.get(i).title());
            assertEquals(albums.get(i).getArtist(), result.get(i).artist());
            assertEquals(albums.get(i).getAge(), result.get(i).age());
            assertEquals(albums.get(i).isAlbumprivate(), result.get(i).albumprivate());
        }
        // Verificar que el método albumRepository.findByUsername() fue invocado exactamente una vez con el nombre de usuario dado
        verify(albumRepository, times(1)).findByUsername(username);
    }
    @Test
    public void testFindByUsername_AlbumsNotFound() {
        // Nombre de usuario para buscar
        String username = "nonExistingUser";
        // Configurar el comportamiento del mock de albumRepository.findByUsername() para devolver una lista vacía
        given(albumRepository.findByUsername(username)).willReturn(Collections.emptyList());
        // Verificar que al llamar al método findByUsername() se lance la excepción esperada
        InsidesoundException exception = assertThrows(InsidesoundException.class, () -> albumService.findByUsername(username));
        // Verificar que el código de estado de la excepción sea correcto
        assertEquals(400, exception.getStatusCode());
        // Verificar que el mensaje de la excepción contenga la descripción asociada en el enum para ALBUM_NOT_FOUND_BY_USERNAME
        assertTrue(exception.getMessage().contains(InsidesoundErrorCode.ALBUM_NOT_FOUND_BY_USERNAME.getErrorMap().get("GET ALBUMS BY USERNAME")));
        // Verificar que el método albumRepository.findByUsername() fue invocado exactamente una vez con el nombre de usuario dado
        verify(albumRepository, times(1)).findByUsername(username);
    }
    @Test
    public void testSave() {
        // Crear un álbum ficticio y un DTO de respuesta de álbum correspondiente
        Album savedAlbum = albums.get(0);
        AlbumResponseDto expectedDto = albumsResponseDto.get(0);
        // Configurar el comportamiento del mock de albumRepository.save() para devolver el álbum guardado
        given(albumRepository.save(any(Album.class))).willReturn(savedAlbum);
        // Llamar al método save() del servicio
        AlbumResponseDto result = albumService.save(albumRequestDto);
        // Verificar que el resultado no sea nulo
        assertNotNull(result);
        // Verificar que el resultado devuelto sea igual al DTO de respuesta de álbum esperado
        assertEquals(expectedDto, result);
        // Verificar que el método albumRepository.save() fue invocado exactamente una vez con cualquier instancia de Album
        verify(albumRepository, times(1)).save(any(Album.class));
    }
    @Test
    public void testUpdate() {
        // ID del álbum a actualizar
        Long id = albums.get(0).getId();
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver el álbum a actualizar
        Album albumToUpdate = albums.get(0);
        given(albumRepository.findById(id)).willReturn(Optional.of(albumToUpdate));
        // Configurar el comportamiento del mock de albumRepository.save() para devolver el álbum actualizado
       // Album updatedAlbum = albums.get(0);
        given(albumRepository.save(any(Album.class))).willReturn(albumToUpdate);
        // Llamar al método update() del servicio
        AlbumResponseDto result = albumService.update(albumRequestDto, id);
        // Verificar que el resultado no sea nulo
        assertNotNull(result);
        // Verificar que el título del álbum actualizado sea el esperado
        assertEquals(albumRequestDto.title(), result.title());
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(albumRepository, times(1)).findById(id);
        // Verificar que el método albumRepository.save() fue invocado exactamente una vez con cualquier instancia de Album
        verify(albumRepository, times(1)).save(any(Album.class));    }

    @Test
    public void testRemove() {
        // ID del álbum a eliminar
        Long id = albums.get(1).getId();
        // Configurar el comportamiento del mock de albumRepository.findById() para devolver un álbum existente
        Album existingAlbum = albums.get(1);
        given(albumRepository.findById(id)).willReturn(Optional.of(existingAlbum));
        // Configurar el comportamiento del mock de trackRepository.removeTracksByAlbumId() para no lanzar ninguna excepción
        doNothing().when(trackRepository).removeTracksByAlbumId(id);
        // Llamar al método remove() del servicio
        albumService.remove(id);
        // Verificar que el método albumRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(albumRepository, times(1)).findById(id);
        // Verificar que el método albumRepository.deleteById() fue invocado exactamente una vez con el ID del álbum existente
        verify(albumRepository, times(1)).deleteById(id);
        // Verificar que el método trackRepository.removeTracksByAlbumId() fue invocado exactamente una vez con el ID del álbum existente
        verify(trackRepository, times(1)).removeTracksByAlbumId(id);
    }
}
