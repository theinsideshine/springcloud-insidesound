package com.theinsideshine.insidesound.backend.users.controllers;

import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService userService, @Value("${pagination.default-size}") Integer defaultPageSize) {
        this.userService = userService;
        this.defaultPageSize = defaultPageSize;
    }

    @GetMapping
    @Operation(summary = "Obtener lista de usuarios", description = "Devuelve una lista con los usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
    })
    public List<UserResponseDto> showlist() {
        return userService.findAll();
    }


    @GetMapping("/page/{page}")
    @Operation(summary = "Obtener lista de usuarios paginada", description = "Devuelve una pagina con los usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
    })
    public Page<UserResponseDto> showlistPageable(@PathVariable Integer page) {
        //int pageSize = defaultPageSize != null ? defaultPageSize : 5;
        return userService.findAllPageable(PageRequest.of(page, 5));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario segun id", description = "Devuelve un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> showUserById(@PathVariable Long id) {
        Optional<UserResponseDto> userOptional = userService.findById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/usernames")
    @Operation(summary = "Obtener lista de los username", description = "Devuelve una lista con los username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
    })
    public List<String> getAllUsernames() {
        return userService.getAllUsernames();
    }

    @PostMapping
    @Operation(summary = "Crea un usuario", description = "Devuelve el usuario creado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Argumentos no validos.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "409", description = "Campo duplicado en base de datos.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.save(userRequestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita un usuario", description = "Devuelve el usuario editado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "El usuario a editar no existe.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "500", description = "El usuario no se pudo editar.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserRequestDtoUpdate userRequestDtoUpdate, @PathVariable Long id) {
        return ResponseEntity.ok(userService.update(userRequestDtoUpdate, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un usuario y borra si tiene tracks y albumes asociados", description = "Devuelve OK.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "No se pudo borrar el usuario.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo borrar el album asociado al username.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo borrar el track asociado al username.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<?> remove(@PathVariable Long id) {
        userService.remove(id);
        return ResponseEntity.ok().build();
    }
}
