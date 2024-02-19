package com.theinsideshine.insidesound.backend.albums.controllers;


import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDTO;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.services.AlbumService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    private static final Logger log = LoggerFactory.getLogger(AlbumController.class);


    @GetMapping
    public List<Album> list() {
        log.info("Got a request");
        return albumService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showAlbum(@PathVariable Long id) {
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

    @GetMapping("/public/{username}")
    public List<Album> getPublicAlbumsByUsername(@PathVariable String username) {
        return albumService.findPublicAlbumsByUsername(username);
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
    public ResponseEntity<?> createAlbum(
            @Valid @ModelAttribute AlbumRequestDTO albumRequest,
            BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return validationFormadata(albumRequest,result);
        }

        byte[] imageBytes = albumRequest.getImageFile().getBytes();


        Album albumDb = new Album();
        albumDb.setUsername(albumRequest.getUsername());
        albumDb.setTitle(albumRequest.getTitle());
        albumDb.setArtist(albumRequest.getArtist());
        albumDb.setAge(albumRequest.getAge());
        albumDb.setAlbumprivate(albumRequest.isAlbumprivate());
        albumDb.setImage(imageBytes);

        Album savedAlbum = albumService.save(albumDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlbum(
            @Valid @ModelAttribute AlbumRequestDTO albumRequest,
            BindingResult result,
            @PathVariable Long id) throws IOException {

        if (result.hasErrors()) {
            return validationFormadata(albumRequest,result);
        }

        byte[] imageBytes = albumRequest.getImageFile().getBytes();


        Optional<Album> o = albumService.findById(id);

        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Album albumDb = o.get();
        albumDb.setUsername(albumRequest.getUsername());
        albumDb.setTitle(albumRequest.getTitle());
        albumDb.setArtist(albumRequest.getArtist());
        albumDb.setAge(albumRequest.getAge());
        albumDb.setAlbumprivate(albumRequest.isAlbumprivate());
        albumDb.setImage(imageBytes);


        Album savedAlbum = albumService.save(albumDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
    }

    /*
     La operacion se considera correcta si borra el album ,sin errores de servidor al intentar buscar
        album_id en tracks que contengan el album a borrar( si encuentra album_id=0)


     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAlbum(@PathVariable Long id) {
        Optional<Album> o = albumService.findById(id);

        if (o.isPresent()) {

            try {
                albumService.remove(id); //
                return ResponseEntity.noContent().build(); // 204
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al borrar albumes: Error en la query de update de album_id "+e.getMessage());
            }

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("username/{username}")
    public ResponseEntity<?> removeAlbumsByUsername(@PathVariable String username) {
        List<Album> albums = albumService.findByUsername(username);

        if (!albums.isEmpty()) {
            try {
                albums.stream()
                        .map(Album::getId)
                        .forEach(albumService::remove);
                return ResponseEntity.ok(new ApiResponse("Albums eliminados exitosamente."));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al eliminar Albums."));
            }
        } else {
            return ResponseEntity.ok(new ApiResponse("No hay Albums para eliminar ."));
        }
    }


    private ResponseEntity<?> validationFormadata(AlbumRequestDTO trackRequest, BindingResult result) {
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

        return ResponseEntity.badRequest().body(errors);
    }

}
