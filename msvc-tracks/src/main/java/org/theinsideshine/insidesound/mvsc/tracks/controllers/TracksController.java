package org.theinsideshine.insidesound.mvsc.tracks.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.theinsideshine.insidesound.mvsc.tracks.models.dto.TrackRequestDTO;
import org.theinsideshine.insidesound.mvsc.tracks.models.entity.Track;
import org.theinsideshine.insidesound.mvsc.tracks.services.TrackService;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class TracksController {

    @Autowired
    private TrackService trackService;

    @GetMapping
    public List<Track> list() {
        return trackService.findAll();
    }

    @GetMapping("/by-album-id/{id}")
    public ResponseEntity<?> showTrackByAlbumId(@PathVariable Long id) {
        List<Track> tracks = trackService.findByAlbumId(id);

        // Se devuelve ok si no hay ninguno asi el front puede poner no hay canciones disponibles
        if (tracks.size() >= 0) {
            return ResponseEntity.ok(tracks);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{trackId}/album")
    public ResponseEntity<Long> getAlbumIdByTrackId(@PathVariable Long trackId) {

        Long albumId = trackService.getAlbumIdByTrackId(trackId);

        if (albumId != null) {
            return ResponseEntity.ok(albumId);
        } else {
            return ResponseEntity.ok(0L);
        }
    }


    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> showTracksByUsername(@PathVariable String username) {
        List<Track> tracks = trackService.findByUsername(username);

        // Se devuelve ok si no hay ninguno asi el front puede poner no hay canciones disponibles
        if (tracks.size() >= 0) {
            return ResponseEntity.ok(tracks);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> showImageTrack(@PathVariable Long id) {


        Optional<Track> o = trackService.findById(id);

        if (o.isEmpty() || o.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        Resource image = new ByteArrayResource(o.get().getImage());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);

    }

    @GetMapping("/mp3/{id}")
    public ResponseEntity<?> showMp3Track(@PathVariable Long id) {

        Optional<Track> o = trackService.findById(id);

        if (o.isEmpty() || o.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        Resource mp3 = new ByteArrayResource(o.get().getMp3());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(mp3);
    }

    @PostMapping
    public ResponseEntity<?> createTrack(
            @Valid @ModelAttribute TrackRequestDTO trackRequest,
            BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return validationFormadata(trackRequest, result);
        }

        byte[] imageBytes = trackRequest.getImageFile().getBytes();
        byte[] mp3Bytes = trackRequest.getMp3File().getBytes();

        Track trackDb = new Track();
        trackDb.setUsername(trackRequest.getUsername());
        trackDb.setTitle(trackRequest.getTitle());
        trackDb.setImage(imageBytes);
        trackDb.setMp3(mp3Bytes);
        trackDb.setAlbum_id(0L);

        Track savedTrack = trackService.save(trackDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrack);

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrack(
            @Valid @ModelAttribute TrackRequestDTO trackRequest,
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
    }

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

    @DeleteMapping("username/{username}")
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
    }


    @PostMapping("/removeTracksByAlbumId/{albumId}")
    public ResponseEntity<?> removeTracksByAlbumId(@PathVariable Long albumId) {
        try {
            trackService.removeTracksByAlbumId(albumId);
            return ResponseEntity.ok(new ApiResponse("Pistas actualizadas exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al actualizar pistas."+e.getMessage()));
        }
    }


    private ResponseEntity<?> validationFormadata(TrackRequestDTO trackRequest, BindingResult result) {
     /*
        La logica de validacion esta escrita para el envio desde React,
        en postman se comporta diferente. UFF!!
        */
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
    }

}
