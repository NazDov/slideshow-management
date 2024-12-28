package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
public class DeleteImageService {
    private final ImageRepo imageRepo;

    public DeleteImageService(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Transactional
    public Mono<Image> delete(Long id) {
        log.info("Deleting image by id, id=[{}]", id);
        return imageRepo.findById(id)
                .flatMap(image -> imageRepo.save(image.copyFrom(image)
                        .isDeleted(true)
                        .build()))
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException(
                                        "Entity not found, id: " + id)));

    }


}
