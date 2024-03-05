package com.theinsideshine.insidesound.backend.users.services;

import java.util.List;
import java.util.Optional;

import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    
    List<UserResponseDto> findAll();

    Page<UserResponseDto> findAll(Pageable pageable);

    Optional<UserResponseDto> findById(Long id);

    List<String> getAllUsernames() ;

    UserResponseDto save(UserRequestDto userRequestDto);
    UserResponseDto update(UserRequestDtoUpdate userRequestDtoUpdate, Long id);

    void remove(Long id);
}
