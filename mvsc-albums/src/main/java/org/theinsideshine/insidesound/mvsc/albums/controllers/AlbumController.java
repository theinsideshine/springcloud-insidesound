package org.theinsideshine.insidesound.mvsc.albums.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.*;

import jakarta.validation.Valid;
import org.theinsideshine.insidesound.mvsc.albums.services.TrackService;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private TrackService trackService;

    @GetMapping
    public List<Album> list() {
        return albumService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Album> albumOptional = albumService.findById(id);

        if (albumOptional.isPresent()) {
            return ResponseEntity.ok(albumOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> showAlbumsByUsername(@PathVariable String username) {
        List<Album> albums = albumService.findByUsername(username);

        if (albums.size()>0 ){
            return ResponseEntity.ok(albums);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> showImageAlbum(@PathVariable Long id) {


        Optional<Album> o = albumService.findById( id);

        if (o.isEmpty() || o.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        Resource image =  new ByteArrayResource(o.get().getImage());
        return  ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);

    }




    @PostMapping
    public ResponseEntity<?> createAlbum(@Valid @ModelAttribute Album album, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        // Guardar el álbum en una transacción para obtener el ID del álbum
        Album savedAlbum = albumService.save(album);

        // Obtener los IDs de los tracks desde el álbum
        List<Long> trackIds = album.getTracksId();
        if (trackIds != null && !trackIds.isEmpty()) {
            for (Long trackId : trackIds) {
                Optional<Track> trackOptional = trackService.findById(trackId);
                if (trackOptional.isPresent()) {
                    Track track = trackOptional.get();
                    track.setAlbum(savedAlbum); // Establecer el álbum en cada track con el ID obtenido
                    trackService.save(track); // Guardar el track con la referencia al álbum
                } else {
                    // Manejar el caso cuando un track con el ID dado no es encontrado
                    // Puedes lanzar una excepción o mostrar un mensaje de error
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Track not found for ID: " + trackId);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlbum(@Valid @ModelAttribute Album album, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()){
            return validation(result);
        }
        Optional<Album> o = albumService.findById(id);

        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Album albumDb = o.get();
        albumDb.setUsername(album.getUsername());
        albumDb.setTitle(album.getTitle());
        albumDb.setArtist(album.getArtist());
        albumDb.setAge(album.getAge());
        albumDb.setImage(album.getImage());
        albumDb.setTracksId(album.getTracksId());

        Album savedAlbum = albumService.save(album);

        // Obtener los IDs de los tracks desde el álbum
        List<Long> trackIds = album.getTracksId();
        if (trackIds != null && !trackIds.isEmpty()) {
            for (Long trackId : trackIds) {
                Optional<Track> trackOptional = trackService.findById(trackId);
                if (trackOptional.isPresent()) {
                    Track track = trackOptional.get();
                    track.setAlbum(savedAlbum); // Establecer el álbum en cada track con el ID obtenido
                    trackService.save(track); // Guardar el track con la referencia al álbum
                } else {
                    // Manejar el caso cuando un track con el ID dado no es encontrado
                    // Puedes lanzar una excepción o mostrar un mensaje de error
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Track not found for ID: " + trackId);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAlbum(@PathVariable Long id) {
        Optional<Album> o = albumService.findById(id);

        if (o.isPresent()) {
            albumService.remove(id);
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
