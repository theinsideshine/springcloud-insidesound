package com.theinsideshine.insidesound.backend.users.repositories;

import com.theinsideshine.insidesound.backend.users.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository
        extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.username=?1")
    Optional<User> getUserByUsername(String username);

    Page<User> findAll(Pageable pageable);

}
