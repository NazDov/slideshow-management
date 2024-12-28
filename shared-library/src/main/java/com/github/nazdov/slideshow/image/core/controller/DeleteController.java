package com.github.nazdov.slideshow.image.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class DeleteController<REQ, E> {

    private final Function<REQ, Mono<E>> commandFunction;
    private final Consumer<REQ> validationFunction;


    public DeleteController(Consumer<REQ> validationFunction,
                            Function<REQ, Mono<E>> commandFunction) {
        this.validationFunction = validationFunction;
        this.commandFunction = commandFunction;
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<?> handleIncomingRequest(@PathVariable("id") REQ incomingRequest) {
        return Mono.just(incomingRequest)
                .doOnNext(req -> log.info("Delete request received, [{}]", req.toString()))
                .doOnNext(validationFunction)
                .doOnNext(req -> log.info("Validation completed for: [{}]", req.toString()))
                .flatMap(commandFunction);
    }
}
