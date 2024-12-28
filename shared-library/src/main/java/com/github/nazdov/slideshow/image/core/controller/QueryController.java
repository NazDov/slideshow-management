package com.github.nazdov.slideshow.image.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class QueryController<REQ, RES> {

    private final Function<REQ, Mono<RES>> queryFunction;
    private final Consumer<REQ> validationFunction;


    public QueryController(Consumer<REQ> validationFunction,
                           Function<REQ, Mono<RES>> queryFunction) {
        this.validationFunction = validationFunction;
        this.queryFunction = queryFunction;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<?> handleIncomingRequest(@RequestParam("query") REQ queryRequest) {
        return Mono.just(queryRequest)
                .doOnNext(req -> log.info("Get request received, [{}]", req.toString()))
                .doOnNext(validationFunction)
                .doOnNext(req -> log.info("Validation completed for: [{}]", req.toString()))
                .flatMap(queryFunction);
    }
}
