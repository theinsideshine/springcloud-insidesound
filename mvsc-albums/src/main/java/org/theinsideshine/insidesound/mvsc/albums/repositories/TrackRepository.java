package org.theinsideshine.insidesound.mvsc.albums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Track;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track,Long> {

    @Query("SELECT t FROM Track t WHERE t.album.id = :albumId")
    List<Track> findByAlbumId(Long albumId);
}
