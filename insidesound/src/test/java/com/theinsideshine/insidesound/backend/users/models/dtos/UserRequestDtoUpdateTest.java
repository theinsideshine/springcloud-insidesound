package com.theinsideshine.insidesound.backend.users.models.dtos;

import com.theinsideshine.insidesound.backend.datas.UserData;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRequestDtoUpdateTest {

    @Test
    void testUserRequestDtoMapperDtoToEntity() throws IOException {
        UserRequestDtoUpdate expectedUserRequestDtoUpdate = UserData.getUserRequestDtoUpdate();
        User realUser = UserRequestDtoUpdate.UserRequestDtoUpdateMapperDtoToEntity(expectedUserRequestDtoUpdate);
        assertEquals(expectedUserRequestDtoUpdate.id(), realUser.getId());
        assertEquals(expectedUserRequestDtoUpdate.username(), realUser.getUsername());
        assertEquals(expectedUserRequestDtoUpdate.email(), realUser.getEmail());
        assertEquals(expectedUserRequestDtoUpdate.admin(), realUser.isAdmin());
    }

    @Test
    void testUserRequestDtoUpdateMapperEntityToDto() {
        User user = UserData.getUser();
        UserRequestDtoUpdate expectedUserRequestDtoUpdate = UserRequestDtoUpdate.UserRequestDtoUpdateMapperEntityToDto(user);
        assertEquals(user.getId(), expectedUserRequestDtoUpdate.id());
        assertEquals(user.getUsername(), expectedUserRequestDtoUpdate.username());
        ;
        assertEquals(user.getEmail(), expectedUserRequestDtoUpdate.email());
        assertEquals(user.isAdmin(), expectedUserRequestDtoUpdate.admin());
    }
}
