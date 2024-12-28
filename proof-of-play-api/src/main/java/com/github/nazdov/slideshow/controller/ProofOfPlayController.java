package com.github.nazdov.slideshow.controller;

import com.github.nazdov.slideshow.image.core.controller.BaseController;
import com.github.nazdov.slideshow.service.ProofOfPlayService;
import com.github.nazdov.slideshow.validation.ProofOfPlayEventValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Tag(
        name = "Proof of Play",
        description = "API for recording proof of play events every time an image is played"
)
@RestController
@RequestMapping("/api/v1/slideshow/{slideshowId}/proof-of-play/{imageId}")
@Slf4j
public class ProofOfPlayController extends BaseController<Tuple2<Long, Long>, Tuple2<Long, Long>, Void, Void> {

    public ProofOfPlayController(ProofOfPlayEventValidator validator,
                                 ProofOfPlayService service) {
        super(validator::validate, data -> service
                        .recordProofOfPlay(data)
                        .doOnNext(res ->
                                log.info("Responding to proof-of-lay request." +
                                                " result=[{}], status=[{}]",
                                        res, HttpStatus.OK
                                )
                        ),
                Mono::just,
                unused -> Mono.empty()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> handleIncomingRequest(
            @PathVariable("slideshowId")
            @Schema(description = "The ID of the slideshow.", example = "1")
            Long slideshowId,
            @PathVariable("imageId")
            @Schema(description = "The ID of the image within the slideshow.", example = "1")
            Long imageId
    ) {
        return handleMonoRequest(
                Mono.just(Tuples.of(slideshowId, imageId))
        );

    }
}
