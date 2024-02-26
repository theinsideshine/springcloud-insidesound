package com.theinsideshine.insidesound.backend.users.models.dto;

import com.theinsideshine.insidesound.backend.users.models.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequestDtoUpdate(
        Long id,

        @NotBlank
        @Size(min = 4, max = 8)
        String username,

        @NotEmpty
        @Email
        String email,


        boolean admin
) {
    public static UserRequestDtoUpdate UserRequestDtoUpdateMapperEntityToDto(User user){
        return new UserRequestDtoUpdate(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isAdmin()
        );
    }

    public static User UserRequestDtoUpdateMapperDtoToEntity(UserRequestDtoUpdate userRequestDto){
        return new User(
                userRequestDto.id(),
                userRequestDto.username(),
                userRequestDto.email(),
                userRequestDto.admin()
        );
    }
}



