package org.theinsideshine.insidesound.mvsc.albums.repositories;

import org.springframework.data.repository.CrudRepository;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;

public interface AlbumRepository extends CrudRepository <Album,Long> {
}
