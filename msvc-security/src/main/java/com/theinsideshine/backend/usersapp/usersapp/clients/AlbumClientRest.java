package com.theinsideshine.backend.usersapp.usersapp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="msvc-albums",url="${msvc.albums.url}")
public interface AlbumClientRest {


    @DeleteMapping("username/{username}")
    ResponseEntity<?> removeAlbumsByUsername(@PathVariable String username);
}