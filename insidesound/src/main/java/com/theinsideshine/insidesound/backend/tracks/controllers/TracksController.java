package com.theinsideshine.insidesound.backend.tracks.controllers;

import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import com.theinsideshine.insidesound.backend.tracks.services.TrackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tracks")
public class TracksController {

    private final TrackService trackService;

    @Autowired
    public TracksController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    public List<TrackResponseDto> list() {
        return trackService.findAll();
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> showImagesTrack(@PathVariable Long id) {
        Resource image = trackService.findImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/mp3/{id}")
    public ResponseEntity<?> showMp3Track(@PathVariable Long id) {
        Resource mp3 = trackService.findImageById(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(mp3);
    }

    @GetMapping("/by-album-id/{id}")
    public List<TrackResponseDto> showTrackByAlbumId(@PathVariable Long id) {
        return trackService.findByAlbumId( id );
    }


    /*
    Devuelve: album_id que tiene asociado un track, busca por id del track
              si no tiene album asociado devuelve 0
     */
    @GetMapping("/{trackId}/album")
    public ResponseEntity<Long> getAlbumIdByTrackId(@PathVariable Long trackId) {
        Long albumId = trackService.getAlbumIdByTrackId(trackId);
        return ResponseEntity.ok(albumId != null ? albumId : 0L);

    }
    /*
     Siempre  devuelve ok ,y si no hay tracks el front puede poner: no hay canciones disponibles
     */
    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<TrackResponseDto>> showTracksByUsername(@PathVariable String username) {
            return ResponseEntity.ok(trackService.findByUsername(username));
    }


    @PostMapping
    public ResponseEntity<TrackResponseDto> create(@Valid @ModelAttribute TrackRequestDto trackRequestDto) {
        return ResponseEntity.ok(trackService.save(trackRequestDto));
    }





   /* @PutMapping("/{id}")
    public ResponseEntity<?> updateTrack(
            @Valid @ModelAttribute TrackRequestDto trackRequest,
            BindingResult result,
            @PathVariable Long id) throws IOException {

        if (result.hasErrors()) {
            return validationFormadata(trackRequest, result);
        }

        byte[] imageBytes = trackRequest.getImageFile().getBytes();
        byte[] mp3Bytes = trackRequest.getMp3File().getBytes();

        Optional<Track> o = trackService.findById(id);

        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Track trackDb = o.get(); // Mantiene el album_id
        trackDb.setUsername(trackRequest.getUsername());
        trackDb.setTitle(trackRequest.getTitle());
        trackDb.setImage(imageBytes);
        trackDb.setMp3(mp3Bytes);


        Track savedTrack = trackService.save(trackDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrack);
    }*/

    // Endpoint para asociar un álbum a una pista
    @PostMapping("/{trackId}/associateAlbum")
    public ResponseEntity<String> associateAlbumToTrack(
            @PathVariable Long trackId,
            @RequestParam Long albumId) {

        // Lógica para asociar el álbum al track usando trackId y albumId
        try {
            trackService.associateAlbumToTrack(trackId, albumId);
            return ResponseEntity.ok("Álbum asociado exitosamente a la pista.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al asociar el álbum a la pista: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTrack(@PathVariable Long id) {
        Optional<Track> o = trackService.findById(id);

        if (o.isPresent()) {
            trackService.remove(id);
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.notFound().build();
    }

    /*@DeleteMapping("username/{username}")
    public ResponseEntity<?> removeTracksByUsername(@PathVariable String username) {
        List<Track> tracks = trackService.findByUsername(username);

        if (!tracks.isEmpty()) {
            try {
                tracks.stream()
                        .map(Track::getId)
                        .forEach(trackService::remove);
                return ResponseEntity.ok(new ApiResponse("Tracks eliminados exitosamente."));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al eliminar tracks."));
            }
        } else {
            return ResponseEntity.ok(new ApiResponse("No hay Tracks para eliminar."));
        }
    }*/


    @PostMapping("/removeTracksByAlbumId/{albumId}")
    public ResponseEntity<?> removeTracksByAlbumId(@PathVariable Long albumId) {
        try {
            trackService.removeTracksByAlbumId(albumId);
            return ResponseEntity.ok(new ApiResponse("Pistas actualizadas exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al actualizar pistas."+e.getMessage()));
        }
    }


    /*private ResponseEntity<?> validationFormadata(TrackRequestDto trackRequest, BindingResult result) {
     *//*
        La logica de validacion esta escrita para el envio desde React,
        en postman se comporta diferente. UFF!!
        *//*
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        if (trackRequest.getImageFile() == null || trackRequest.getImageFile().isEmpty()) {
            errors.put("imageFile", "El archivo no puede estar vacio.");
        }
        if (trackRequest.getMp3File() == null || trackRequest.getMp3File().isEmpty()) {
            errors.put("mp3File", "El archivo no puede estar vacio.");
        }
        return ResponseEntity.badRequest().body(errors);
    }*/

}
