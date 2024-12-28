package com.github.nazdov.slideshow.mapper;

import com.github.nazdov.slideshow.dto.AddSlideshowRequest;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.mapper.DataMapper;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AddSlideshowRequestMapper implements DataMapper<AddSlideshowRequest,
        Mono<Tuple3<Slideshow, List<Long>, List<String>>>> {

    @Override
    public Mono<Tuple3<Slideshow, List<Long>, List<String>>> mapFrom(AddSlideshowRequest type) {
        return Mono.just(
                Tuples.of(
                        Slideshow.builder()
                                .name(type.getName())
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Optional.ofNullable(type.getImageIds()).orElseGet(List::of),
                        Optional.ofNullable(type.getImageUrls()).orElseGet(List::of))
        );
    }

}
