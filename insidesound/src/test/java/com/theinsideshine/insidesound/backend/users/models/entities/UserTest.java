package com.theinsideshine.insidesound.backend.users.models.entities;

import com.theinsideshine.insidesound.backend.datas.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {


    @Test
    void testConstructorsUser() {
        User user = new User(1L, "john_doe", "password123", "john.doe@example.com", true);
        User user1 = new User(1L, "john_doe", "john.doe@example.com", true);
        assertNotNull(user.getId(), () -> "El Id no puede ser nula");
        assertNotNull(user1.getId(), () -> "El Id debe ser nula");
    }


    @Test
    void testIdUser() {
        User user = UserData.getUser();
        Long expectedId = 65L;
        user.setId(expectedId);
        Long realId = user.getId();
        assertNotNull(realId, () -> "El Id no puede ser nula");
        assertTrue(realId.equals(expectedId), () -> "Id debe ser igual a la expectedId");
    }

    @Test
    void testUsernameUser() {
        User user = UserData.getUser();
        String expectedUsername = "Lucio";
        user.setUsername(expectedUsername);
        String realUsername = user.getUsername();
        assertNotNull(realUsername, () -> "El Username no puede ser nula");
        assertTrue(realUsername.equals(expectedUsername), () -> "Username debe ser igual a la expectedUsername");
    }

    @Test
    void testPasswordUser() {
        User user = UserData.getUser();
        String expectedPassword = "123Lucio";
        user.setPassword(expectedPassword);
        String realPassword = user.getPassword();
        assertNotNull(realPassword, () -> "El Password no puede ser nula");
        assertTrue(realPassword.equals(expectedPassword), () -> "Password debe ser igual a la expectedPassword");
    }

    @Test
    void testEmailUser() {
        User user = UserData.getUser();
        String expectedMail = "Lucio@gmail.com";
        user.setEmail(expectedMail);
        String realEmail = user.getEmail();
        assertNotNull(realEmail, () -> "El Email no puede ser nula");
        assertTrue(realEmail.equals(expectedMail), () -> "Email debe ser igual a la expectedPassword");
    }

    @Test
    void testAdminUser() {
        User user = UserData.getUser();
        Boolean expectedAdmin = true;
        user.setAdmin(expectedAdmin);
        Boolean realAdmin = user.isAdmin();
        assertNotNull(realAdmin, () -> "El Admin no puede ser nula");
        assertTrue(realAdmin, () -> "Admin debe ser igual a la expectedAdmin");
    }

    @Test
    void testSetRoles() {
        User user = new User();

        // Crear una lista de roles simulada
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "ROLE_ADMIN"));
        roles.add(new Role(2L, "ROLE_USER"));

        // Llamar al método setRoles para establecer los roles del usuario
        user.setRoles(roles);

        // Verificar que los roles se establecieron correctamente
        assertEquals(2, user.getRoles().size());  // Verificar el tamaño de la lista de roles
        assertTrue(user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName())));  // Verificar la presencia del rol "ROLE_ADMIN"
        assertTrue(user.getRoles().stream().anyMatch(r -> "ROLE_USER".equals(r.getName())));  // Verificar la presencia del rol "ROLE_USER"
    }


}


