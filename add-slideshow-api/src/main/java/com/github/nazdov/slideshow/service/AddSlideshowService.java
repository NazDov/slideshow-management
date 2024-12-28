package com.github.nazdov.slideshow.service;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.List;

@Slf4j
public class AddSlideshowService {
    private final ImageRepo imageRepo;
    private final SlideshowRepo slideshowRepo;

    public AddSlideshowService(SlideshowRepo slideshowRepo,
                               ImageRepo imageRepo) {
        this.slideshowRepo = slideshowRepo;
        this.imageRepo = imageRepo;
    }

    @Transactional
    public Mono<Slideshow> add(Tuple3<Slideshow, List<Long>, List<String>> slideshowData) {

        return slideshowRepo.save(slideshowData.getT1())
                .zipWith(getImageFlux(slideshowData).collectList())
                .flatMap(tuple -> {

                            if (tuple.getT2().isEmpty()) {
                                return Mono.error(new EntityNotFoundException("No images found by either Ids or Urls."));
                            }

                            return imageRepo.saveAll(tuple.getT2().stream()
                                            .map(image -> image
                                                    .copyFrom(image)
                                                    .slideshowId(tuple.getT1().getId())
                                                    .build())
                                            .toList())
                                    .collectList()
                                    .thenReturn(tuple.getT1());
                        }

                )
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                "No images found by either Ids or Urls")));
    }

    private Flux<Image> getImageFlux(Tuple3<Slideshow, List<Long>, List<String>> slideshowData) {
        List<String> imageUrls = slideshowData.getT3();
        List<Long> imageIdVals = slideshowData.getT2();

        return ObjectUtils.isEmpty(imageIdVals)
                ? imageRepo.findAllByUrls(imageUrls)
                : imageRepo.findAllById(imageIdVals);
    }
}
