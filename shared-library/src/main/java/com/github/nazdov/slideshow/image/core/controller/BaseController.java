package com.github.nazdov.slideshow.image.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @param <REQ>  request controller type
 * @param <E>    input type for service method
 * @param <R>    output type for service method
 * @param <RES>  response controller type
 */
@Slf4j
public abstract class BaseController<REQ, E, R, RES> {

    private final Function<E, Mono<R>> commandFunction;
    private final Consumer<REQ> validationFunction;
    private final Function<REQ, Mono<E>> requestMapperFunction;
    private final Function<R, Mono<RES>> responseMapperFunction;

    public BaseController(Consumer<REQ> validationFunction,
                          Function<E, Mono<R>> commandFunction,
                          Function<REQ, Mono<E>> requestMapperFunction,
                          Function<R, Mono<RES>> responseMapperFunction) {
        this.validationFunction = validationFunction;
        this.commandFunction = commandFunction;
        this.requestMapperFunction = requestMapperFunction;
        this.responseMapperFunction = responseMapperFunction;
    }
    public Mono<RES> handleIncomingRequest(@RequestBody Mono<REQ> incomingRequest) {
        return handleMonoRequest(incomingRequest);
    };

    protected Mono<RES> handleMonoRequest(Mono<REQ> incomingRequest) {
        return incomingRequest
                .doOnNext(req -> log.info("Request received, [{}]", req.toString()))
                .doOnNext(validationFunction)
                .flatMap(requestMapperFunction)
                .flatMap(commandFunction)
                .flatMap(responseMapperFunction)
                .doOnNext(response ->
                        log.info("Responding to request. status=[{}], body=[{}]",
                                HttpStatus.CREATED, response));
    }
}
