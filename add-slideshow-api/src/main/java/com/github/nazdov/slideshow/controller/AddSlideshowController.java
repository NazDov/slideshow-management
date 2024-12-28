package com.github.nazdov.slideshow.controller;

import com.github.nazdov.slideshow.dto.AddSlideshowRequest;
import com.github.nazdov.slideshow.dto.AddSlideshowResponse;
import com.github.nazdov.slideshow.mapper.AddSlideshowRequestMapper;
import com.github.nazdov.slideshow.mapper.AddSlideshowResponseMapper;
import com.github.nazdov.slideshow.service.AddSlideshowService;
import com.github.nazdov.slideshow.validation.AddSlideshowEventValidator;
import com.github.nazdov.slideshow.image.core.controller.PostController;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.function.Tuple3;

import java.util.List;

@Tag(
        name = "Slideshow",
        description = "Manage slideshows including adding new slideshows" +
                " to the system. This API allows the user to add slideshows" +
                " with associated images and metadata.")

@RestController
@RequestMapping("/api/v1/slideshow")
@Slf4j
public class AddSlideshowController extends PostController<AddSlideshowRequest, Tuple3<Slideshow, List<Long>, List<String>>, Slideshow, AddSlideshowResponse> {

    public AddSlideshowController(AddSlideshowEventValidator validator,
                                  AddSlideshowService slideshowService,
                                  AddSlideshowRequestMapper requestMapper,
                                  AddSlideshowResponseMapper responseMapper) {
        super(
                validator::validate,
                slideshowService::add,
                requestMapper::mapFrom,
                responseMapper::mapFrom
        );
    }
}
