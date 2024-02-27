package com.theinsideshine.insidesound.backend.users.controllers;

import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final Integer defaultPageSize;

    public UserController(UserService userService, @Value("${pagination.default-size}") Integer defaultPageSize) {
        this.userService = userService;
        this.defaultPageSize = defaultPageSize;
    }

    @GetMapping
    public List<UserResponseDto> list() {

        return userService.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<UserResponseDto> list(@PathVariable Integer page) {

        return userService.findAll(PageRequest.of(page, defaultPageSize));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<UserResponseDto> userOptional = userService.findById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/usernames")
    public List<String> getAllUsernames() {

        return userService.getAllUsernames();
    }


    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto userRequestDto) {

        return ResponseEntity.ok(userService.save(userRequestDto));

    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserRequestDtoUpdate userRequestDtoUpdate, @PathVariable Long id) {

        return ResponseEntity.ok(userService.update(userRequestDtoUpdate,id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        userService.remove(id);
        return ResponseEntity.ok().build();
    }

}
