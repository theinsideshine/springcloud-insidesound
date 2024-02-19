package com.theinsideshine.insidesound.backend.users.services;

import java.util.List;
import java.util.Optional;

import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.models.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    
    List<UserRequestDto> findAll();

    Page<UserRequestDto> findAll(Pageable pageable);

    Optional<UserRequestDto> findById(Long id);

    public List<String> getAllUsernames() ;

    UserRequestDto save(User user);
    Optional<UserRequestDto> update(UserRequest user, Long id);

    void remove(Long id, String username);
}
