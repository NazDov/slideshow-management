package com.github.nazdov.slideshow.image.core.producer;

import reactor.core.publisher.Mono;

public interface ReactiveEventProducer<T> {

    Mono<Void> produce(T event);
}
