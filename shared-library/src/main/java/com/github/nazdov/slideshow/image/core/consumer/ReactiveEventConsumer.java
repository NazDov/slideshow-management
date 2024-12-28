package com.github.nazdov.slideshow.image.core.consumer;

import reactor.core.publisher.Flux;

public interface ReactiveEventConsumer<T> {
    Flux<String> consume(T topic);
}
