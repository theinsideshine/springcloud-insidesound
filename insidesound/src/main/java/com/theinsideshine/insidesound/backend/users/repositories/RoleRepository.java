package com.theinsideshine.insidesound.backend.users.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.theinsideshine.insidesound.backend.users.models.entities.Role;

public interface RoleRepository
        extends CrudRepository<Role, Long> {
        Optional<Role> findByName(String name);
}
