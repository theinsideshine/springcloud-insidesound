package org.theinsideshine.insidesound.mvsc.albums.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "msvc-tracks", url = "${msvc.tracks.url}")
public interface TrackClientRest {

    @PostMapping("/removeTracksByAlbumId/{albumId}")
    ResponseEntity<?> removeTracksByAlbumId(@PathVariable Long albumId);
}
