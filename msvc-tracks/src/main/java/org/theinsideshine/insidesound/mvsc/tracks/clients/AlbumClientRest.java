package org.theinsideshine.insidesound.mvsc.tracks.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.theinsideshine.insidesound.mvsc.tracks.models.Album;

@FeignClient(name="msvc-albums",url="${msvc.albums.url}")
public interface AlbumClientRest {


    @GetMapping("/{id}")
    ResponseEntity<Album> getAlbumById(@PathVariable Long id);
}
