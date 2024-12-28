package com.github.nazdov.slideshow.image.controller;

import com.github.nazdov.slideshow.image.core.controller.DeleteController;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.service.DeleteImageService;
import com.github.nazdov.slideshow.image.validation.DeleteImageEventValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Image Deletion",
        description = "APIs for managing image deletions by ID"
)
@RestController
@RequestMapping("/api/v1/image/{id}")
@Slf4j
public class DeleteImageController extends DeleteController<Long, Image> {

    public DeleteImageController(DeleteImageEventValidator validator,
                                 DeleteImageService service) {
        super(validator::validate, req -> service.delete(req)
                .doOnNext(
                        consume ->
                                log.info("Responding to delete request. status=[{}], id=[{}]",
                                        HttpStatus.NO_CONTENT, req)));
    }
}
