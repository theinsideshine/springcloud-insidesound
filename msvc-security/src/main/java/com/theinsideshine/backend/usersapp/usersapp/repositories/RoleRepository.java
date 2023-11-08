package com.theinsideshine.backend.usersapp.usersapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.theinsideshine.backend.usersapp.usersapp.models.entities.Role;

public interface RoleRepository
        extends CrudRepository<Role, Long> {
        Optional<Role> findByName(String name);
}
