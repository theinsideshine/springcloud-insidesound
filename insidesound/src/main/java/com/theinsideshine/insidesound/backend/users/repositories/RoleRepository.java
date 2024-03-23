package com.theinsideshine.insidesound.backend.users.repositories;

import com.theinsideshine.insidesound.backend.users.models.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository
        extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
