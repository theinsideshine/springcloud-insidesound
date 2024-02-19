package com.theinsideshine.insidesound.backend.users.models.dto.mapper;

import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;

public class DtoMapperUser {

    private User user;
    
    private DtoMapperUser() {
    }

    public static DtoMapperUser builder() {
        return new DtoMapperUser();
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserRequestDto build() {
        if (user == null) {
            throw new RuntimeException("Debe pasar el entity user!");
        }
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
        return new UserRequestDto(this.user.getId(), user.getUsername(), user.getEmail(), isAdmin);
    }
    

}
