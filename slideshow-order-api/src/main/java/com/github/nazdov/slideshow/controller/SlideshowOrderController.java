package com.github.nazdov.slideshow.controller;

import com.github.nazdov.slideshow.dto.SlideshowOrderResponse;
import com.github.nazdov.slideshow.image.core.controller.TwoReqParamController;
import com.github.nazdov.slideshow.service.SlideshowOrderService;
import com.github.nazdov.slideshow.validation.SlideshowOrderEventValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(
        name = "Slideshow Order API",
        description = "This API allows clients to retrieve the order of slides in a given slideshow" +
                " by its ID and order by date."
)
@RestController
@RequestMapping("/api/v1/slideshow/{id}")
@Slf4j
public class SlideshowOrderController extends TwoReqParamController<String, SlideshowOrderResponse> {
    public SlideshowOrderController(SlideshowOrderEventValidator validator,
                                    SlideshowOrderService service) {
        super(validator::validate, query -> service
                .search(query)
                .doOnNext(res ->
                        log.info("Responding to slideshow order request. result=[{}], status=[{}]",
                                res, HttpStatus.OK)
                )
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<?> handleIncomingRequest(
            @Schema(description = "The ID of the slideshow.", example = "1")
            @PathVariable("id") String id,
            @Schema(description = "The order of image retrieval ASC or DESC.", example = "ASC")
            @RequestParam("order") String orderParam) {
        return handleMonoRequest(Mono.zip(Mono.just(id), Mono.just(orderParam)));
    }
}
