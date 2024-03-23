package com.theinsideshine.insidesound.backend.users.models.dto;

import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.users.models.entities.User;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        boolean admin

) {
    public static UserResponseDto userResponseDtoMapperEntityToDto(User user) {

        if (user == null) {
            throw new InsidesoundException(InsidesoundErrorCode.ERR_USER_NULL);
        }

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        return new UserResponseDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                isAdmin);
    }


}
