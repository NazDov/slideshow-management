package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
public class AddImageService {
    private final ImageRepo imageRepo;

    public AddImageService(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Transactional
    public Mono<Image> add(Image image) {
        return imageRepo.save(image)
                .onErrorResume(DuplicateKeyException.class,
                        throwable -> Mono.error(
                                new ValidationException(
                                        "Image with this id or url already exists")
                        )
                );
    }
}
