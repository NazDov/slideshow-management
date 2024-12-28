package com.github.nazdov.slideshow.image.controller;

import com.github.nazdov.slideshow.image.core.controller.PostController;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.mapper.AddImageResponseMapper;
import com.github.nazdov.slideshow.image.service.AddImageService;
import com.github.nazdov.slideshow.image.validation.AddImageEventValidator;
import com.github.nazdov.slideshow.image.dto.AddImageRequest;
import com.github.nazdov.slideshow.image.dto.AddImageResponse;
import com.github.nazdov.slideshow.image.mapper.AddImageRequestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Image",
        description = "API for saving Image by URL"
)
@RestController
@RequestMapping("/api/v1/image")
@Slf4j

public class AddImageController extends PostController<AddImageRequest, Image, Image, AddImageResponse> {


    public AddImageController(AddImageEventValidator validator,
                              AddImageService addImageService,
                              AddImageRequestMapper requestMapper,
                              AddImageResponseMapper responseMapper) {
        super(
                validator::validate,
                addImageService::add,
                requestMapper::mapFrom,
                responseMapper::mapFrom);
    }
}
