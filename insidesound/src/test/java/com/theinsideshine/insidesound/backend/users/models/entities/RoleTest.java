package com.theinsideshine.insidesound.backend.users.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RoleTest {

    @Test
    public void testCreateRoleWithConstructor() {
        // Crear un objeto Role utilizando el constructor Role(String name)
        Role role = new Role("ROLE_ADMIN");

        // Verificar que el nombre se haya establecido correctamente
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    public void testSetAndGetId() {
        // Crear un objeto Role
        Role role = new Role();

        // Establecer un ID específico utilizando el método setId
        role.setId(1L);

        // Verificar que el ID se haya establecido correctamente
        assertEquals(1L, role.getId());
    }

    @Test
    public void testSetName() {
        // Crear un objeto Role
        Role role = new Role();

        // Establecer el nombre utilizando el método setName
        role.setName("ROLE_USER");

        // Verificar que el nombre se haya establecido correctamente
        assertEquals("ROLE_USER", role.getName());
    }


}


