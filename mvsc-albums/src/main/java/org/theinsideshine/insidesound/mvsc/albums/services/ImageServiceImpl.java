package org.theinsideshine.insidesound.mvsc.albums.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Album;
import org.theinsideshine.insidesound.mvsc.albums.models.entity.Image;
import org.theinsideshine.insidesound.mvsc.albums.repositories.AlbumRepository;
import org.theinsideshine.insidesound.mvsc.albums.repositories.ImageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements  ImageService{

    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Image> findAll() {
        List<Image> images = (List<Image>) imageRepository.findAll();

        return images;
    }

    @Override
    public Optional<Image> findById(Long id) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> update(Image image, Long id) {
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {

    }
}
