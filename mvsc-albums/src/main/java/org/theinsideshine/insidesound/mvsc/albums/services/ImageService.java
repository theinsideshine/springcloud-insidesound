package org.theinsideshine.insidesound.mvsc.albums.services;

import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Image;

import java.util.List;
import java.util.Optional;


public interface ImageService {

    List<Image> findAll();

    Optional<Image> findById(Long id);
    Image save(Image image);
    Optional<Image> update(Image image, Long id);

    void remove(Long id);
}
