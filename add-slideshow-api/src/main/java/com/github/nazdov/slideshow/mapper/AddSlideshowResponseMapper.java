package com.github.nazdov.slideshow.mapper;

import com.github.nazdov.slideshow.dto.AddSlideshowResponse;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.mapper.DataMapper;
import reactor.core.publisher.Mono;

public class AddSlideshowResponseMapper implements DataMapper<Slideshow, Mono<AddSlideshowResponse>> {
    @Override
    public Mono<AddSlideshowResponse> mapFrom(Slideshow type) {
        return Mono.just(
                AddSlideshowResponse.builder()
                        .id(type.getId())
                        .createdAt(type.getCreatedAt())
                        .build()
        );
    }
}
