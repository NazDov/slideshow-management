package com.github.nazdov.slideshow.service;

import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
public class DeleteSlideshowService {
    private final SlideshowRepo repo;

    public DeleteSlideshowService(SlideshowRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public Mono<Slideshow> delete(Long id) {
        log.info("Deleting slideshow by id, id=[{}]", id);
        return repo.findById(id)
                .flatMap(slideshow -> repo.save(slideshow
                        .copyFrom(slideshow)
                        .isDeleted(true)
                        .build()))
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException(
                                        "Slideshow not found, id: " + id)));

    }


}
