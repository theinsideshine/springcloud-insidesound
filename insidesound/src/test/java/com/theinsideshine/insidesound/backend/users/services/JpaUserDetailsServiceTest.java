package com.theinsideshine.insidesound.backend.users.services;

import com.theinsideshine.insidesound.backend.users.models.entities.Role;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JpaUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserDetailsService userDetailsService;

    @Test
    public void testLoadUserByUsername_UserExists() {
        // Crear un rol mockeado
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        // Crear un usuario mockeado con el rol asignado
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        List<Role> roles = Collections.singletonList(roleUser);
        mockUser.setRoles(roles);
        when(userRepository.getUserByUsername("testuser")).thenReturn(Optional.of(mockUser));
        // Ejecutar el método loadUserByUsername
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        // Verificar que se obtenga el UserDetails esperado
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
        // Verificar que se haya llamado al método getUserByUsername del repositorio
        verify(userRepository, times(1)).getUserByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsername_UserDoesNotExist() {
        when(userRepository.getUserByUsername("nonexistentuser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistentuser"));
        verify(userRepository, times(1)).getUserByUsername("nonexistentuser");
    }
}

