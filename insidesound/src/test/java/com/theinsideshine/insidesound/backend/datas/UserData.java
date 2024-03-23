package com.theinsideshine.insidesound.backend.datas;


import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.models.entities.Role;
import com.theinsideshine.insidesound.backend.users.models.entities.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserData {

    public static List<User> getUsersList() {
        List<User> users = Arrays.asList(
                new User(1L, "john_doe", "password123", "john.doe@example.com", true),
                new User(2L, "jane_smith", "secret456", "jane.smith@example.com", false)
        );
        return users;
    }

    public static User getUser() {
        User user = new User(1L, "john_doe", "password123", "john.doe@example.com", true);
        return user;
    }

    public static User getUserWithAdminRole() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        User adminUser = new User(1L, "john_doe", "password123", "john.doe@example.com", true);
        adminUser.getRoles().add(adminRole);
        return adminUser;
    }

    public static List<UserResponseDto> getUsersResponseDtoList() {
        List<User> users = getUsersList();
        List<UserResponseDto> usersResponseDto = users.stream()
                .map(UserResponseDto::userResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());

        return usersResponseDto;
    }

    public static UserResponseDto getuserResponseDto() {
        UserResponseDto userResponseDto =
                new UserResponseDto(1L, "john_doe", "john.doe@example.com", true);
        return userResponseDto;
    }

    public static UserRequestDto getUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto(1L, "john_doe", "password123", "john.doe@example.com", true);
        return userRequestDto;
    }

    public static UserRequestDtoUpdate getUserRequestDtoUpdate() {
        UserRequestDtoUpdate userRequestDtoUpdate = new UserRequestDtoUpdate(1L, "john_doe", "john.doe@example.com", true);
        return userRequestDtoUpdate;
    }
}