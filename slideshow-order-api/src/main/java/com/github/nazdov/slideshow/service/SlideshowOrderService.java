package com.github.nazdov.slideshow.service;

import com.github.nazdov.slideshow.dto.SlideshowImageResponse;
import com.github.nazdov.slideshow.dto.SlideshowOrder;
import com.github.nazdov.slideshow.dto.SlideshowOrderResponse;
import com.github.nazdov.slideshow.image.core.entity.ImageAndSlideshow;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SlideshowOrderService {
    private final SlideshowRepo slideshowRepo;

    public SlideshowOrderService(SlideshowRepo slideshowRepo) {
        this.slideshowRepo = slideshowRepo;
    }

    @Transactional(readOnly = true)
    public Mono<SlideshowOrderResponse> search(Tuple2<String, String> queryData) {
        Long slideshowId = parseSlideshowId(queryData.getT1());

        Mono<List<ImageAndSlideshow>> imagesMono =
                (SlideshowOrder.valueOf(queryData.getT2().toUpperCase()) == SlideshowOrder.ASC
                        ? slideshowRepo.findOrderedAsc(slideshowId)
                        : slideshowRepo.findOrderedDesc(slideshowId))
                        .switchIfEmpty(
                                Mono.error(
                                        new EntityNotFoundException(
                                                "Slideshow not found by id: " + queryData.getT1())))
                        .collectList();

        return imagesMono
                .doOnNext(imageAndSlideshow -> log.info("Found images: {}", imageAndSlideshow))
                .map(this::mapToSlideshowOrderResponse);
    }

    private Long parseSlideshowId(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new InputMismatchException("Invalid slideshow ID format: " + id);
        }
    }

    private SlideshowOrderResponse mapToSlideshowOrderResponse(List<ImageAndSlideshow> imageAndSlideshow) {
        if (imageAndSlideshow.isEmpty()) {
            throw new EntityNotFoundException("No images found for the slideshow.");
        }

        ImageAndSlideshow firstImageAndSlideshow = ObjectUtils.requireNonEmpty(
                imageAndSlideshow.get(0));

        List<SlideshowImageResponse> images = imageAndSlideshow.stream()
                .map(image -> SlideshowImageResponse.builder()
                        .id(image.getImageId())
                        .url(image.getImageUrl())
                        .title(image.getImageTitle())
                        .description(image.getImageDescription())
                        .duration(image.getImageDuration())
                        .createdAt(image.getImageCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return SlideshowOrderResponse.builder()
                .name(firstImageAndSlideshow.getSlideshowName())
                .createdAt(firstImageAndSlideshow.getSlideshowCreatedAt())
                .images(images)
                .build();
    }
}
