package com.github.nazdov.slideshow.image.mapper;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.mapper.DataMapper;
import com.github.nazdov.slideshow.image.dto.AddImageRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class AddImageRequestMapper implements DataMapper<AddImageRequest, Mono<Image>> {
    @Override
    public Mono<Image> mapFrom(AddImageRequest type) {
        return Mono.just(
                Image.builder()
                        .url(type.getUrl())
                        .title(type.getTitle())
                        .description(type.getDescription())
                        .duration(type.getDuration())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
