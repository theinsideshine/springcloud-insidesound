package com.theinsideshine.insidesound.backend.tracks.repositories;

import com.theinsideshine.insidesound.backend.tracks.models.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query("SELECT t FROM Track t WHERE t.album_id = :albumId")
    List<Track> findByAlbumId(Long albumId);


    @Query("SELECT t FROM Track t WHERE t.username = :username")
    List<Track> findByUsername(String username);

    @Query("SELECT t FROM Track t WHERE t.username = :username")
    List<Track> findByAlbumId(String username);

    @Modifying
    @Transactional
    @Query("UPDATE Track t SET t.album_id = 0 WHERE t.album_id = ?1")
    void removeTracksByAlbumId(Long albumId);
}
