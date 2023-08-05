package org.theinsideshine.insidesound.mvsc.albums.repositories;

import org.springframework.data.repository.CrudRepository;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Image;

public interface ImageRepository extends CrudRepository <Image,Long> {
}
