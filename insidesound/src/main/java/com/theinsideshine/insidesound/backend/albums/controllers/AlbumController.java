package com.theinsideshine.insidesound.backend.albums.controllers;


import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumRequestDto;
import com.theinsideshine.insidesound.backend.albums.models.dto.AlbumResponseDto;
import com.theinsideshine.insidesound.backend.albums.models.entity.Album;
import com.theinsideshine.insidesound.backend.albums.services.AlbumService;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.insidesound.InsidesoundException;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<AlbumResponseDto> list() {
        return albumService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showAlbum(@PathVariable Long id) {
        Optional<AlbumResponseDto> albumOptional = albumService.findById(id);
        return albumOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> showImagesAlbum(@PathVariable Long id) {
        Resource image = albumService.findImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/by-username/{username}")
    public List<AlbumResponseDto> showAlbumsByUsername(@PathVariable String username) {
        return albumService.findByUsername( username);
    }

    @GetMapping("/public/{username}")
    public List<AlbumResponseDto> getPublicAlbumsByUsername(@PathVariable String username) {
        return albumService.findPublicAlbumsByUsername(username);
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDto> create(@Valid @ModelAttribute AlbumRequestDto albumRequestDto) {
        return ResponseEntity.ok(albumService.save(albumRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponseDto> update(@Valid @ModelAttribute AlbumRequestDto albumRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(albumService.update(albumRequestDto,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        albumService.remove(id);
        return ResponseEntity.ok().build();
    }
}
