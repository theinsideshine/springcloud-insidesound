package org.theinsideshine.insidesound.mvsc.albums.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<?> showTrackByUsername(@PathVariable String username) {
        List<Track> tracks = trackService.findByUsername(username);

        if (tracks.size()>0 ){
            return ResponseEntity.ok(tracks);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> showImageTrack(@PathVariable Long id) {


        Optional<Track> o = trackService.findById( id);

        if (o.isEmpty() || o.get().getImage() == null) {
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
            @Valid @RequestParam("username") String username,
            @Valid @RequestParam("title") String title,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("mp3File") MultipartFile mp3File
    ) throws IOException {
        if (imageFile.isEmpty() || mp3File.isEmpty()) {
            return ResponseEntity.badRequest().body("Image or MP3 file is empty");
        }

        byte[] imageBytes = imageFile.getBytes();
        byte[] mp3Bytes = mp3File.getBytes();

        Track trackDb = new Track();
        trackDb.setUsername(username);
        trackDb.setTitle(title);
        trackDb.setImage(imageBytes);
        trackDb.setMp3(mp3Bytes);

        Track savedTrack = trackService.save(trackDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrack);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrack(
            @Valid @RequestParam("username") String username,
            @Valid @RequestParam("title") String title,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("mp3File") MultipartFile mp3File,
            @PathVariable Long id
    ) throws IOException {
        if (imageFile.isEmpty() || mp3File.isEmpty()) {
            return ResponseEntity.badRequest().body("Image or MP3 file is empty");
        }

        byte[] imageBytes = imageFile.getBytes();
        byte[] mp3Bytes = mp3File.getBytes();

        Optional<Track> o = trackService.findById(id);

        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Track trackDb = o.get();
        trackDb.setUsername(username);
        trackDb.setTitle(title);
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

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
