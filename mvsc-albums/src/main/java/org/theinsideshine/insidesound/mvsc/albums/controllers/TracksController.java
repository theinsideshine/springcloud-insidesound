package org.theinsideshine.insidesound.mvsc.albums.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;
import org.theinsideshine.insidesound.mvsc.albums.services.AlbumService;
import org.theinsideshine.insidesound.mvsc.albums.services.TrackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tracks")
public class TracksController {

    @Autowired
    private TrackService trackService;

    @GetMapping
    public List<Track> list() {
        return trackService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        List<Track> tracks = trackService.findByAlbumId(id);

        if (tracks.size()>0 ){
            return ResponseEntity.ok(tracks);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Track track, BindingResult result) {
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(trackService.save(track));
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
