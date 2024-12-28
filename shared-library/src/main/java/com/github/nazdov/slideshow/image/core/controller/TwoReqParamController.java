package com.github.nazdov.slideshow.image.core.controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class TwoReqParamController<REQ, RES> extends BaseController<Tuple2<REQ, REQ>, Tuple2<REQ, REQ>, RES, RES> {

    public TwoReqParamController(Consumer<Tuple2<REQ, REQ>> validationFunction,
                                 Function<Tuple2<REQ, REQ>, Mono<RES>> getFunction) {
        super(validationFunction, getFunction, Mono::just, Mono::just);
    }

}
