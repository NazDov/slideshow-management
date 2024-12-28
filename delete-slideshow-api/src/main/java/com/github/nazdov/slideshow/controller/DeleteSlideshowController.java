package com.github.nazdov.slideshow.controller;

import com.github.nazdov.slideshow.image.core.controller.DeleteController;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.service.DeleteSlideshowService;
import com.github.nazdov.slideshow.validation.DeleteSlideshowEventValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Slideshow Deletion",
        description = "APIs for managing slideshows deletions by Id"
)
@RestController
@RequestMapping("/api/v1/slideshow/{id}")
@Slf4j
public class DeleteSlideshowController extends DeleteController<Long, Slideshow> {

    public DeleteSlideshowController(DeleteSlideshowEventValidator validator,
                                     DeleteSlideshowService service) {
        super(validator::validate, req -> service.delete(req)
                .doOnNext(
                        consume ->
                                log.info("Responding to delete request." +
                                                " status=[{}], id=[{}]",
                                        HttpStatus.NO_CONTENT, req)
                )
        );
    }
}
