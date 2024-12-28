package com.github.nazdov.slideshow.image.controller;

import com.github.nazdov.slideshow.image.core.controller.QueryController;
import com.github.nazdov.slideshow.image.dto.SearchResponse;
import com.github.nazdov.slideshow.image.service.SearchService;
import com.github.nazdov.slideshow.image.validation.SearchQueryEventValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Image Search",
        description = "Provides functionality for searching images based on specified query"
)
@RestController
@RequestMapping("/api/v1/image")
@Slf4j
public class SearchController extends QueryController<String, SearchResponse> {
    public SearchController(SearchQueryEventValidator validator,
                            SearchService service) {
        super(validator::validate, query -> service
                .search(query)
                .doOnNext(res ->
                        log.info("Responding to query request. result=[{}], status=[{}]",
                                res, HttpStatus.OK)
                )
        );
    }
}
