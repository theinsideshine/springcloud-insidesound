package com.theinsideshine.insidesound.backend.tracks.controllers;

import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackRequestDto;
import com.theinsideshine.insidesound.backend.tracks.models.dto.TrackResponseDto;
import com.theinsideshine.insidesound.backend.tracks.services.TrackService;
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
        Resource mp3 = trackService.findMp3ById(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<TrackResponseDto> update(@Valid @ModelAttribute TrackRequestDto trackRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(trackService.update(trackRequestDto,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        trackService.remove(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint para asociar un álbum a una pista
    @PostMapping("/{trackId}/associateAlbum")
    public ResponseEntity<String> associateAlbumToTrack(
            @PathVariable Long trackId,
            @RequestParam Long albumId) {
        trackService.associateAlbumToTrack(trackId, albumId);
        return ResponseEntity.ok("Álbum asociado exitosamente a la pista.");
    }



}
