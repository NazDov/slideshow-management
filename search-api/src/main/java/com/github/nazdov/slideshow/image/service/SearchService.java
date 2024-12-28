package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.core.entity.ImageAndSlideshow;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.dto.ImageResponse;
import com.github.nazdov.slideshow.image.dto.SearchResponse;
import com.github.nazdov.slideshow.image.dto.SlideshowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SearchService {
    private final ImageRepo imageRepo;

    public SearchService(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Transactional(readOnly = true)
    public Mono<SearchResponse> search(String query) {

        return imageRepo
                .searchByQuery(query)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                "Images not found by search query: " + query)))
                .collectList()
                .map(imageAndSlideshows -> toSearchResponse(query, imageAndSlideshows));
    }

    private SearchResponse toSearchResponse(String query,
                                            List<ImageAndSlideshow> imageAndSlideshows) {
        return SearchResponse.builder()
                .query(query)
                .totalResults(imageAndSlideshows.size())
                .images(toImageResponse(imageAndSlideshows))
                .build();
    }

    private List<ImageResponse> toImageResponse(List<ImageAndSlideshow> imageAndSlideshows) {
        return imageAndSlideshows.stream()
                .map(imageAndSlideshow -> ImageResponse.builder()
                        .id(imageAndSlideshow.getImageId())
                        .url(imageAndSlideshow.getImageUrl())
                        .title(imageAndSlideshow.getImageTitle())
                        .description(imageAndSlideshow.getImageDescription())
                        .duration(imageAndSlideshow.getImageDuration())
                        .slideshow(SlideshowResponse.builder()
                                .id(imageAndSlideshow.getSlideshowId())
                                .createdAt(imageAndSlideshow.getSlideshowCreatedAt())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
