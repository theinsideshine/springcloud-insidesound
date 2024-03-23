package com.theinsideshine.insidesound.backend.users.models.dtos;

import com.theinsideshine.insidesound.backend.datas.UserData;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRequestDtoTest {

    @Test
    void testUserRequestDtoMapperDtoToEntity() throws IOException {
        UserRequestDto expectedUserRequestDto = UserData.getUserRequestDto();
        User realUser = UserRequestDto.UserRequestDtoMapperDtoToEntity(expectedUserRequestDto);
        assertEquals(expectedUserRequestDto.id(), realUser.getId());
        assertEquals(expectedUserRequestDto.username(), realUser.getUsername());
        assertEquals(expectedUserRequestDto.email(), realUser.getEmail());
        assertEquals(expectedUserRequestDto.password(), realUser.getPassword());
        assertEquals(expectedUserRequestDto.admin(), realUser.isAdmin());

    }

    @Test
    void testUserRequestDtoMapperEntityToDto() {
        User user = UserData.getUser();
        UserRequestDto expectedUserRequestDto = UserRequestDto.UserRequestDtoMapperEntityToDto(user);
        assertEquals(user.getId(), expectedUserRequestDto.id());
        assertEquals(user.getUsername(), expectedUserRequestDto.username());
        assertEquals(user.getPassword(), expectedUserRequestDto.password());
        assertEquals(user.getEmail(), expectedUserRequestDto.email());
        assertEquals(user.isAdmin(), expectedUserRequestDto.admin());
    }
}
