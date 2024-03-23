package com.theinsideshine.insidesound.backend.albums.controllers;


import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.services.AlbumService;
import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumController {


    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }


    @GetMapping
    @Operation(summary = "Obtener lista de albumes", description = "Devuelve una lista con los albumes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
    })
    public List<AlbumResponseDto> showList() {
        return albumService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un album segun id", description = "Devuelve un album.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> showAlbumById(@PathVariable Long id) {
        Optional<AlbumResponseDto> albumOptional = albumService.findById(id);
        return albumOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/img/{id}")
    @Operation(summary = "Obtener una imagen segun albumId", description = "Devuelve una imagen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "El id del album no tiene imagen.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<?> showImageByAlbumId(@PathVariable Long id) {
        Resource image = albumService.findImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/by-username/{username}")
    @Operation(summary = "Obtener albumes segun username", description = "Devuelve una lista de album. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
            @ApiResponse(responseCode = "400", description = " El username no tiene albums.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public List<AlbumResponseDto> showAlbumsByUsername(@PathVariable String username) {
        return albumService.findByUsername(username);
    }

    @GetMapping("/public/by-username/{username}")
    @Operation(summary = "Obtener albumes publicos segun username", description = "Devuelve una lista de album. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
            @ApiResponse(responseCode = "400", description = " El username no tiene albums publicos.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public List<AlbumResponseDto> getPublicAlbumsByUsername(@PathVariable String username) {
        return albumService.findPublicAlbumsByUsername(username);
    }

    @PostMapping
    @Operation(summary = "Crea un album", description = "Devuelve el album creado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Argumentos no validos.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "409", description = "Campo duplicado en base de datos.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<AlbumResponseDto> create(@Valid @ModelAttribute AlbumRequestDto albumRequestDto) {
        return ResponseEntity.ok(albumService.save(albumRequestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita un album", description = "Devuelve el album editado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "El album a editar no existe.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "409", description = "Campo duplicado en base de datos.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<AlbumResponseDto> update(@Valid @ModelAttribute AlbumRequestDto albumRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(albumService.update(albumRequestDto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un album y borra la relacion si tiene tracks asociados", description = "Devuelve OK.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "No se pudo borrar el album.", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo borrar el track asociado al album.", content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<?> remove(@PathVariable Long id) {
        albumService.remove(id);
        return ResponseEntity.ok().build();
    }


}
