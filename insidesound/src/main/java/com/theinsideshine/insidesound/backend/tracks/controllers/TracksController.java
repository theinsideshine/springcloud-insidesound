package com.theinsideshine.insidesound.backend.tracks.controllers;

import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.exceptions.ErrorModel;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.services.TrackService;
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

@RestController
@RequestMapping("/tracks")
public class TracksController {

    private final TrackService trackService;

    @Autowired
    public TracksController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    @Operation(summary = "Obtener lista de tracks", description = "Devuelve una lista con los tracks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TrackResponseDto.class))),
    })
    public List<TrackResponseDto> showList() {
        return trackService.findAll();
    }

    @GetMapping("/img/{id}")
    @Operation(summary = "Obtener una imagen segun trackId", description = "Devuelve una imagen. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "El id del track no tiene imagen." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<?> showImagesByTrackId(@PathVariable Long id) {
        Resource image = trackService.findImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/mp3/{id}")
    @Operation(summary = "Obtener una mp3 segun trackId", description = "Devuelve una mp3. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "El id del track no tiene mp3." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<?> showMp3ByTrackId(@PathVariable Long id) {
        Resource mp3 = trackService.findMp3ById(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(mp3);
    }

    @GetMapping("/by-album-id/{id}")
    @Operation(summary = "Busca todos los tracks que tiene asocidado albumId", description = "Devuelve una lista de tracks. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TrackResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "No existe track con el albumId asociado." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public List<TrackResponseDto> showTrackByAlbumId(@PathVariable Long id) {
        return trackService.findByAlbumId( id );
    }


    /*
    Devuelve: album_id que tiene asociado un track, busca por id del track
              si no tiene album asociado devuelve 0
     */
    @GetMapping("/{trackId}/album")
    @Operation(summary = "Busca por trackId", description = "Devuelve el albumId asociado al track, si no existe devuelve 0L ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            })
    public ResponseEntity<Long> getAlbumIdByTrackId(@PathVariable Long trackId) {
        Long albumId = trackService.getAlbumIdByTrackId(trackId);
        return ResponseEntity.ok(albumId != null ? albumId : 0L);

    }
    /*
     Siempre  devuelve ok ,y si no hay tracks el front puede poner: no hay canciones disponibles
     */
    @GetMapping("/by-username/{username}")
    @Operation(summary = "Busca por username ", description = "Devuelve una lista de track ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TrackResponseDto.class))),
    })
    public ResponseEntity<List<TrackResponseDto>> showTracksByUsername(@PathVariable String username) {
            return ResponseEntity.ok(trackService.findByUsername(username));
    }


    @PostMapping
    @Operation(summary = "Crea un track", description = "Devuelve el track creado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TrackResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Argumentos no validos." ,content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "409", description = "Campo duplicado en base de datos." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<TrackResponseDto> create(@Valid @ModelAttribute TrackRequestDto trackRequestDto) {
        return ResponseEntity.ok(trackService.save(trackRequestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita un track", description = "Devuelve el track editado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TrackResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "El track a editar no existe." ,content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "500", description = "El track no se pudo editar." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<TrackResponseDto> update(@Valid @ModelAttribute TrackRequestDto trackRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(trackService.update(trackRequestDto,id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un track", description = "Devuelve OK.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "No se pudo borrar el track." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<?> remove(@PathVariable Long id) {
        trackService.remove(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint para asociar un álbum a una pista
    @PostMapping("/{trackId}/associateAlbum")
    @Operation(summary = "Asocia un albumId a un track", description = "Devuelve OK.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TrackResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "El id no tiene track." ,content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "400", description = "El id no tiene album" ,content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "500", description = "El track no se pudo editar." ,content = @Content(schema = @Schema(implementation = ErrorModel.class)))
    })
    public ResponseEntity<String> associateAlbumToTrack(
            @PathVariable Long trackId,
            @RequestParam Long albumId) {
        trackService.associateAlbumToTrack(trackId, albumId);
        return ResponseEntity.ok("Álbum asociado exitosamente a la pista.");
    }



}
