package com.theinsideshine.insidesound.backend.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theinsideshine.insidesound.backend.datas.UserData;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.repositories.UserRepository;
import com.theinsideshine.insidesound.backend.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void testShowlist() throws Exception {
        // Crear una lista de usuarios simulada
        List<UserResponseDto> usersResponseDtoList = UserData.getUsersResponseDtoList();
        // Configurar el comportamiento del mock userService.findAll() para devolver la lista simulada
        when(userService.findAll()).thenReturn(usersResponseDtoList);
        // Realizar la solicitud GET al endpoint del controlador y verificar la respuesta
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))  // Verificar que se devuelvan dos usuarios en la lista
                .andExpect(jsonPath("$[0].id").value(1L))  // Verificar el ID del primer usuario
                .andExpect(jsonPath("$[0].username").value("john_doe"))  // Verificar el nombre de usuario del primer usuario
                .andExpect(jsonPath("$[1].id").value(2L))  // Verificar el ID del segundo usuario
                .andExpect(jsonPath("$[1].username").value("jane_smith"));  // Verificar el nombre de usuario del segundo usuario
        // Verificar que el método userService.findAll() se invocó una vez
        verify(userService, times(1)).findAll();
    }

    @Test
    public void testShowlistPageable() throws Exception {
        // Crear una lista simulada de usuarios
        List<UserResponseDto> userResponseDtoList = UserData.getUsersResponseDtoList();
        // Configurar el comportamiento del mock userService.findAll() para devolver una página simulada
        Page<UserResponseDto> userResponseDtoPage = new PageImpl<>(userResponseDtoList);
        when(userService.findAllPageable(any(Pageable.class))).thenReturn(userResponseDtoPage);
        // Realizar la solicitud GET al endpoint del controlador y verificar la respuesta
        mockMvc.perform(get("/users/page/{page}", 0))  // Suponiendo que se desea probar la página 0
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))  // Verificar el tamaño de la lista de usuarios en la página
                .andExpect(jsonPath("$.content[0].id").value(1L))  // Verificar el ID del primer usuario en la página
                .andExpect(jsonPath("$.content[0].username").value("john_doe"))  // Verificar el nombre de usuario del primer usuario en la página
                .andExpect(jsonPath("$.content[1].id").value(2L))  // Verificar el ID del segundo usuario en la página
                .andExpect(jsonPath("$.content[1].username").value("jane_smith"));  // Verificar el nombre de usuario del segundo usuario en la página
        // Verificar que el método userService.findAll() se invocó una vez con una PageRequest
        verify(userService, times(1)).findAllPageable(any(PageRequest.class));
    }

    @Test
    public void testShowUserById_UserExists() throws Exception {
        // Mockear la respuesta del servicio para un usuario existente
        UserResponseDto mockUserResponseDto = UserData.getuserResponseDto();
        when(userService.findById(anyLong())).thenReturn(Optional.of(mockUserResponseDto));
        // Realizar la solicitud GET al endpoint del controlador con un ID existente
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))  // Verificar el ID del usuario en la respuesta
                .andExpect(jsonPath("$.username").value("john_doe"));  // Verificar el nombre de usuario en la respuesta
    }

    @Test
    public void testShowUserById_UserNotFound() throws Exception {
        // Mockear la respuesta del servicio para un usuario que no existe
        when(userService.findById(anyLong())).thenReturn(Optional.empty());
        // Realizar la solicitud GET al endpoint del controlador con un ID que no existe
        mockMvc.perform(get("/users/{id}", 100L))
                .andExpect(status().isNotFound());  // Verificar que se reciba un código de respuesta 404 (Not Found)
    }

    @Test
    public void testGetAllUsernames() throws Exception {
        // Obtener la lista de nombres de usuario ficticios desde UserData
        List<String> mockUsernames = UserData.getUsersList().stream()
                .map(User::getUsername)
                .toList();
        // Mockear la respuesta del userService para devolver la lista de nombres de usuario ficticios
        when(userService.getAllUsernames()).thenReturn(mockUsernames);
        // Configurar el mockMvc con el userController
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        // Realizar la solicitud GET al endpoint y verificar la respuesta
        mockMvc.perform(get("/users/usernames"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())  // Verificar que la respuesta sea un array JSON
                .andExpect(jsonPath("$", hasSize(mockUsernames.size())))  // Verificar el tamaño del array
                .andExpect(jsonPath("$[0]").value(mockUsernames.get(0)))  // Verificar el primer nombre de usuario
                .andExpect(jsonPath("$[1]").value(mockUsernames.get(1)));  // Verificar el segundo nombre de usuario
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        // Crear un usuario ficticio de solicitud utilizando UserData
        UserRequestDto userRequestDto = UserData.getUserRequestDto();
        // Mockear la respuesta del servicio para un usuario creado correctamente
        UserResponseDto mockUserResponseDto = UserData.getuserResponseDto();
        when(userService.save(userRequestDto)).thenReturn(mockUserResponseDto);
        // Realizar la solicitud POST al endpoint del controlador con el usuario ficticio de solicitud
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserResponseDto.id()))  // Verificar el ID del usuario en la respuesta
                .andExpect(jsonPath("$.username").value(mockUserResponseDto.username()));  // Verificar el nombre de usuario en la respuesta
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        // Crear un usuario ficticio de solicitud de actualización utilizando UserData
        UserRequestDtoUpdate userRequestDtoUpdate = UserData.getUserRequestDtoUpdate();
        Long userId = 1L; // ID del usuario a actualizar
        // Mockear la respuesta del servicio para un usuario actualizado correctamente
        UserResponseDto mockUpdatedUserResponseDto = UserData.getuserResponseDto();
        when(userService.update(userRequestDtoUpdate, userId)).thenReturn(mockUpdatedUserResponseDto);
        // Realizar la solicitud PUT al endpoint del controlador con el usuario ficticio de solicitud
        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUpdatedUserResponseDto.id())) // Verificar el ID del usuario en la respuesta
                .andExpect(jsonPath("$.username").value(mockUpdatedUserResponseDto.username()));  // Verificar el nombre de usuario en la respuesta
    }

    @Test
    public void testRemoveUser_Success() throws Exception {
        Long userId = 1L; // ID del usuario a eliminar

        // Simular la eliminación exitosa del usuario en el servicio
        doNothing().when(userService).remove(userId);

        // Realizar la solicitud DELETE al endpoint del controlador para eliminar el usuario
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk()); // Verificar que se recibe un código de respuesta OK
    }
}



