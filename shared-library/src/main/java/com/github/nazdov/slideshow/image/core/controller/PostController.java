package com.github.nazdov.slideshow.image.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class PostController<REQ, E, R, RES> extends BaseController<REQ, E, R, RES> {

    public PostController(Consumer<REQ> validationFunction,
                          Function<E, Mono<R>> commandFunction,
                          Function<REQ, Mono<E>> requestMapperFunction,
                          Function<R, Mono<RES>> responseMapperFunction) {
        super(validationFunction, commandFunction, requestMapperFunction, responseMapperFunction);
    }


    @Override
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RES> handleIncomingRequest(@RequestBody Mono<REQ> incomingRequest) {
        return handleMonoRequest(incomingRequest);
    }
}
