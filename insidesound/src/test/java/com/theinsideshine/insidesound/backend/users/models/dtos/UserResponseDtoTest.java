package com.theinsideshine.insidesound.backend.users.models.dtos;

import com.theinsideshine.insidesound.backend.datas.UserData;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserResponseDtoTest {

    @Test
    void testUserResponseDtoMapperEntityToDto() {
        User user = UserData.getUserWithAdminRole();
        UserResponseDto expectedUserResponseDto = UserResponseDto.userResponseDtoMapperEntityToDto(user);
        assertEquals(user.getId(), expectedUserResponseDto.id());
        assertEquals(user.getUsername(), expectedUserResponseDto.username());
        assertEquals(user.getEmail(), expectedUserResponseDto.email());
        assertEquals(user.isAdmin(), expectedUserResponseDto.admin());

    }

    @Test
    void testUserResponseDtoMapperEntityToDto_NullUser() {
        assertThrows(InsidesoundException.class, () -> {
            UserResponseDto.userResponseDtoMapperEntityToDto(null);
        });
    }
}