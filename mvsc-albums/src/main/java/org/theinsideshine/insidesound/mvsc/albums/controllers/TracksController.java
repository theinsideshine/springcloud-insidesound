package org.theinsideshine.insidesound.mvsc.albums.controllers;

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
import org.springframework.web.multipart.MultipartFile;
import org.theinsideshine.insidesound.mvsc.albums.models.dto.TrackRequestDTO;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;
import org.theinsideshine.insidesound.mvsc.albums.services.AlbumService;
import org.theinsideshine.insidesound.mvsc.albums.services.TrackService;

import java.io.IOException;
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

    @GetMapping("/by-album-id/{id}")
    public ResponseEntity<?> showTrackByAlbumId(@PathVariable Long id) {
        List<Track> tracks = trackService.findByAlbumId(id);

        if (tracks.size()>0 ){
            return ResponseEntity.ok(tracks);
        }
        return ResponseEntity.notFound().build();
    }




    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> showTracksByUsername(@PathVariable String username) {
        List<Track> tracks = trackService.findByUsername(username);

        if (tracks.size()>0 ){
            return ResponseEntity.ok(tracks);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> showImageTrack(@PathVariable Long id) {


        Optional<Track> o = trackService.findById( id);

        if (o.isEmpty() || o.get().getImage()== null) {
            return ResponseEntity.notFound().build();
        }
        Resource image =  new ByteArrayResource(o.get().getImage());
        return  ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);

    }

    @GetMapping("/mp3/{id}")
    public ResponseEntity<?> showMp3Track(@PathVariable Long id) {

        Optional<Track> o = trackService.findById( id);

        if (o.isEmpty() || o.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        Resource mp3 =  new ByteArrayResource(o.get().getMp3());
        return  ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(mp3);
    }

    @PostMapping
    public ResponseEntity<?> createTrack(
            @Valid @ModelAttribute TrackRequestDTO trackRequest,
            BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return validationFormadata(trackRequest,result);
        }

        byte[] imageBytes = trackRequest.getImageFile().getBytes();
        byte[] mp3Bytes = trackRequest.getMp3File().getBytes();

        Track trackDb = new Track();
        trackDb.setUsername(trackRequest.getUsername());
        trackDb.setTitle(trackRequest.getTitle());
        trackDb.setImage(imageBytes);
        trackDb.setMp3(mp3Bytes);

        Track savedTrack = trackService.save(trackDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrack);

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrack(
            @Valid @ModelAttribute TrackRequestDTO trackRequest,
            BindingResult result,
            @PathVariable Long id) throws IOException {

        if (result.hasErrors()) {
            return validationFormadata(trackRequest,result);
        }

        byte[] imageBytes = trackRequest.getImageFile().getBytes();
        byte[] mp3Bytes = trackRequest.getMp3File().getBytes();

        Optional<Track> o = trackService.findById(id);

        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Track trackDb = o.get();
        trackDb.setUsername(trackRequest.getUsername());
        trackDb.setTitle(trackRequest.getTitle());
        trackDb.setImage(imageBytes);
        trackDb.setMp3(mp3Bytes);

        Track savedTrack = trackService.save(trackDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrack);
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

    private ResponseEntity<?> validationFormadata(TrackRequestDTO trackRequest,BindingResult result) {
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
