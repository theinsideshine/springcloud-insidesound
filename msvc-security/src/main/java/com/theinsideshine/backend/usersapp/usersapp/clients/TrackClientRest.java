package com.theinsideshine.backend.usersapp.usersapp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "msvc-tracks")
public interface TrackClientRest {

    @DeleteMapping("username/{username}")
    ResponseEntity<?> removeTracksByUsername(@PathVariable String username);
}