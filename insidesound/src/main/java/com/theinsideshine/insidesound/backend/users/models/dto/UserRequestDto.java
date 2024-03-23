package com.theinsideshine.insidesound.backend.users.models.dto;

import com.theinsideshine.insidesound.backend.users.models.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        Long id,
        @NotBlank
        @Size(min = 4, max = 8)
        String username,

        @NotBlank
        String password,

        @NotEmpty
        @Email
        String email,
        boolean admin
) {
    public static UserRequestDto UserRequestDtoMapperEntityToDto(User user) {
        return new UserRequestDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.isAdmin()
        );
    }

    public static User UserRequestDtoMapperDtoToEntity(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.id(),
                userRequestDto.username(),
                userRequestDto.password(),
                userRequestDto.email(),
                userRequestDto.admin()
        );
    }
}



