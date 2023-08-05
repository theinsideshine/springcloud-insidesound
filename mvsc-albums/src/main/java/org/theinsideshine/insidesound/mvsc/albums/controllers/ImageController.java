package org.theinsideshine.insidesound.mvsc.albums.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Image;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;
import org.theinsideshine.insidesound.mvsc.albums.services.AlbumService;
import org.theinsideshine.insidesound.mvsc.albums.services.ImageService;
import org.theinsideshine.insidesound.mvsc.albums.services.ImageServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public List<Image> list() {
        return imageService.findAll();
    }


    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Image image, BindingResult result) {
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.save(image));
    }




        @PostMapping("/create-with-image")
        public ResponseEntity<?> createWithImage(@Valid Image image,
                                                 BindingResult result,
                                                 @RequestParam("foto") MultipartFile foto
                                                 ) throws IOException {
            if (result.hasErrors()) {
                return validation(result);
            }

            // Procesar la imagen
            if (!foto.isEmpty()) {
                image.setFoto(foto.getBytes());
            }



            return create(image, result);
        }





    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
