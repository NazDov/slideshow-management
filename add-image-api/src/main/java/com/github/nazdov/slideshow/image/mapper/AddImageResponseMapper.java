package com.github.nazdov.slideshow.image.mapper;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.mapper.DataMapper;
import com.github.nazdov.slideshow.image.dto.AddImageResponse;
import reactor.core.publisher.Mono;

public class AddImageResponseMapper implements DataMapper<Image, Mono<AddImageResponse>> {
    @Override
    public Mono<AddImageResponse> mapFrom(Image type) {
        return Mono.just(
                AddImageResponse.builder()
                        .id(type.getId())
                        .createdAt(type.getCreatedAt())
                        .build()
        );
    }
}
