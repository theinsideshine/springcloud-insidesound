package com.theinsideshine.insidesound.backend.users.services;

import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.repositories.AlbumRepository;
import com.theinsideshine.insidesound.backend.datas.AlbumData;
import com.theinsideshine.insidesound.backend.datas.TrackData;
import com.theinsideshine.insidesound.backend.datas.UserData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.tracks.repositories.TrackRepository;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.models.entities.Role;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.repositories.RoleRepository;
import com.theinsideshine.insidesound.backend.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {
    private List<UserResponseDto> usersResponseDto;
    private UserRequestDto userRequestDto;
    private List<User> users;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private TrackRepository trackRepository;
    @Mock
    private AlbumRepository albumRepository;

    @Test
    public void testFindAll() {
        // Obtener un usuario simulado usando UserData
        User user = UserData.getUserWithAdminRole();
        // Configurar el comportamiento simulado del repositorio
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        // Llamar al método del servicio y verificar el resultado
        List<UserResponseDto> result = userService.findAll();
        // Verificar que el resultado contiene al usuario simulado
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).id());
        assertEquals(user.getUsername(), result.get(0).username());
        assertEquals(user.getEmail(), result.get(0).email());
        assertEquals(user.isAdmin(), result.get(0).admin());
    }

    @Test
    public void testFindAllPageable() {
        // Obtener un usuario simulado usando UserData
        User user = UserData.getUser();
        // Crear una lista simulada de usuarios y una página simulada
        List<User> userList = Collections.singletonList(user);
        Page<User> userPage = new PageImpl<>(userList);
        // Configurar el comportamiento simulado del repositorio
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        // Llamar al método del servicio y verificar el resultado
        Page<UserResponseDto> resultPage = userService.findAllPageable(PageRequest.of(0, 10));
        // Verificar que el resultado tenga los datos esperados
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(1, resultPage.getContent().size());
        assertEquals(user.getUsername(), resultPage.getContent().get(0).username());
        // Agregar más aserciones según sea necesario para verificar otros campos de UserResponseDto
    }

    @Test
    public void testFindById() {
        // Obtener un usuario simulado usando UserData
        User user = UserData.getUserWithAdminRole();
        // Configurar el comportamiento simulado del repositorio para devolver el usuario simulado por ID
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // Llamar al método del servicio para buscar por ID y verificar el resultado
        Optional<UserResponseDto> result = userService.findById(user.getId());
        // Verificar que el resultado no esté vacío y tenga los datos esperados
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().username());
        assertEquals(user.getEmail(), result.get().email());
        assertEquals(user.isAdmin(), result.get().admin());
    }

    @Test
    public void testGetAllUsernames() {
        // Obtener una lista simulada de usuarios usando UserData
        List<User> userList = UserData.getUsersList();
        // Configurar el comportamiento simulado del repositorio para devolver la lista de usuarios simulados
        when(userRepository.findAll()).thenReturn(userList);
        // Llamar al método del servicio y verificar el resultado
        List<String> result = userService.getAllUsernames();
        // Verificar que el resultado tenga los nombres de usuario esperados
        assertEquals(2, result.size());
        assertTrue(result.contains("john_doe"));
        assertTrue(result.contains("jane_smith"));
    }

    @Test
    public void testSaveUser() {
        // Crear un usuario simulado utilizando UserData
        UserRequestDto userRequestDto = UserData.getUserRequestDto();
        // Mockear el comportamiento de roleRepository.findByName para isAdmin false
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        // Mockear el comportamiento de userRepository.save
        when(userRepository.save(any(User.class)))
                .thenReturn(new User(1L, "john_doe", "password123", "john.doe@example.com", true));
        // Mockear el comportamiento de passwordEncoder.encode
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // Ejecutar el método save
        UserResponseDto savedUser = userService.save(userRequestDto);
        // Verificar que el usuario devuelto sea el esperado
        assertNotNull(savedUser);
        assertEquals(1L, savedUser.id());
        assertEquals("john_doe", savedUser.username());
        assertEquals("john.doe@example.com", savedUser.email());
        // Verificar que se haya llamado a roleRepository.findByName con "ROLE_USER"
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        // Verificar que se haya llamado a userRepository.save con el usuario correcto
        verify(userRepository, times(1)).save(any(User.class));
        // Verificar que se haya llamado a passwordEncoder.encode con la contraseña correcta
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    public void testUpdateUser() {
        // Crear un usuario simulado existente
        User existingUser = UserData.getUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        // Mockear el comportamiento de roleRepository.findByName para isAdmin true
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(new Role(1L, "ROLE_ADMIN")));
        // Mockear el comportamiento de userRepository.save
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        // Ejecutar el método update
        UserRequestDtoUpdate userRequestDtoUpdate = UserData.getUserRequestDtoUpdate();
        UserResponseDto updatedUser = userService.update(userRequestDtoUpdate, 1L);
        // Verificar que el usuario actualizado sea el esperado
        assertNotNull(updatedUser);
        assertEquals(1L, updatedUser.id());
        assertEquals("john_doe", updatedUser.username());
        assertEquals("john.doe@example.com", updatedUser.email());
        assertTrue(updatedUser.admin());
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // Verificar que se haya llamado a roleRepository.findByName con "ROLE_ADMIN"
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        // Verificar que se haya llamado a userRepository.save con el usuario actualizado
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_ExceptionHandling() {
        // Crear un usuario simulado existente
        User existingUser = UserData.getUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        // Mockear el comportamiento de roleRepository.findByName para isAdmin true
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(new Role(1L, "ROLE_ADMIN")));
        // Simular que userRepository.save lanza una excepción al intentar guardar el usuario actualizado
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Error saving user"));
        // Ejecutar el método update
        UserRequestDtoUpdate userRequestDtoUpdate = UserData.getUserRequestDtoUpdate();
        // Utilizar assertThrows para verificar que se lanza la excepción esperada
        assertThrows(InsidesoundException.class, () -> userService.update(userRequestDtoUpdate, 1L));
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // Verificar que se haya llamado a roleRepository.findByName con "ROLE_ADMIN"
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        // Verificar que se haya llamado a userRepository.save con el usuario actualizado
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Simular que no se encuentra el usuario con ID 1 en el repositorio
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // Ejecutar el método update y utilizar assertThrows para verificar que se lanza la excepción esperada
        assertThrows(InsidesoundException.class, () -> userService.update(UserData.getUserRequestDtoUpdate(), 1L));
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // No es necesario verificar otros métodos en este caso ya que el usuario no se encuentra en el repositorio
    }

    @Test
    public void testRemoveUser_UserExists() {
        // Simular que el usuario con ID 1 existe en el repositorio
        User existingUser = UserData.getUser();
        Long existinId = existingUser.getId();
        String existingUsername = existingUser.getUsername();
        List<Album> albums = AlbumData.getAlbumsList();
        when(userRepository.findById(existinId)).thenReturn(Optional.of(existingUser));
        when(albumRepository.findByUsername(existingUsername)).thenReturn((albums));
        List<Track> tracks = TrackData.getTrackList();
        when(trackRepository.findByUsername(existingUsername)).thenReturn((tracks));
        // Ejecutar el método remove
        userService.remove(existinId);
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // Verificar que se haya llamado a los métodos de eliminación correspondientes
        verify(userRepository, times(1)).deleteById(existingUser.getId());
        verify(albumRepository, times(1)).findByUsername(existingUser.getUsername());
        verify(trackRepository, times(1)).findByUsername(existingUser.getUsername());
    }

    @Test
    public void testRemoveUser_UserExistsButDeletionFails() {
        // Simular que el usuario con ID 1 existe en el repositorio
        User existingUser = UserData.getUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        // Simular que userRepository.deleteById lanza EmptyResultDataAccessException al intentar eliminar el usuario
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(existingUser.getId());
        // Ejecutar el método remove y utilizar assertThrows para verificar que se lanza la excepción esperada
        assertThrows(InsidesoundException.class, () -> userService.remove(1L));
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // Verificar que se haya llamado a userRepository.deleteById con el ID correcto
        verify(userRepository, times(1)).deleteById(existingUser.getId());
    }

    @Test
    public void testRemoveAlbumsByUser_AlbumDeletionFails() {
        // Simular que el usuario con ID 1 existe en el repositorio
        User existingUser = UserData.getUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        // Simular que se encuentran álbumes asociados al usuario
        List<Album> albums = AlbumData.getAlbumsList();
        when(albumRepository.findByUsername(existingUser.getUsername())).thenReturn(albums);
        // Simular que albumRepository.deleteById lanza una excepción al intentar eliminar un álbum
        doThrow(EmptyResultDataAccessException.class).when(albumRepository).deleteById(albums.get(0).getId());
        // Ejecutar el método remove y utilizar assertThrows para verificar que se lanza la excepción esperada
        assertThrows(InsidesoundException.class, () -> userService.remove(1L));
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // Verificar que se haya llamado a albumRepository.findByUsername con el nombre de usuario correcto
        verify(albumRepository, times(1)).findByUsername(existingUser.getUsername());
        // Verificar que se haya llamado a albumRepository.deleteById para cada álbum encontrado
        albums.forEach(album -> verify(albumRepository, times(1)).deleteById(albums.get(0).getId()));
    }

    @Test
    public void testRemoveTracksByUser_TrackDeletionFails() {
        // Simular que el usuario con ID 1 existe en el repositorio
        User existingUser = UserData.getUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        // Simular que se encuentran pistas asociadas al usuario
        List<Track> tracks = TrackData.getTrackList();
        when(trackRepository.findByUsername(existingUser.getUsername())).thenReturn(tracks);
        // Simular que trackRepository.deleteById lanza una excepción al intentar eliminar una pista
        doThrow(EmptyResultDataAccessException.class).when(trackRepository).deleteById(tracks.get(0).getId());
        // Ejecutar el método remove y utilizar assertThrows para verificar que se lanza la excepción esperada
        assertThrows(InsidesoundException.class, () -> userService.remove(1L));
        // Verificar que se haya llamado a userRepository.findById con el ID correcto
        verify(userRepository, times(1)).findById(1L);
        // Verificar que se haya llamado a trackRepository.findByUsername con el nombre de usuario correcto
        verify(trackRepository, times(1)).findByUsername(existingUser.getUsername());
        // Verificar que se haya llamado a trackRepository.deleteById para cada pista encontrada
        tracks.forEach(track -> verify(trackRepository, times(1)).deleteById(tracks.get(0).getId()));
    }

    @Test
    public void testRemove_IdNotFound() {
        // ID de un track que no existe
        Long nonExistentId = 999L;
        // Configurar el comportamiento del mock de trackRepository.findById() para devolver un Optional vacío
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        // Llamar al método remove() del servicio
        userService.remove(nonExistentId);
        // Verificar que el método trackRepository.findById() fue invocado exactamente una vez con el ID dado
        verify(userRepository, times(1)).findById(nonExistentId);

    }

}





