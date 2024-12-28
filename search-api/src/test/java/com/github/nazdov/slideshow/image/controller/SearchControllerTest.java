package com.github.nazdov.slideshow.image.controller;

import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.dto.ImageResponse;
import com.github.nazdov.slideshow.image.dto.SearchResponse;
import com.github.nazdov.slideshow.image.dto.SlideshowResponse;
import com.github.nazdov.slideshow.image.service.SearchService;
import com.github.nazdov.slideshow.image.validation.SearchQueryEventValidator;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@WebFluxTest(controllers = SearchController.class)
@ExtendWith(SpringExtension.class)
@Import({TestConfig.class})
@ActiveProfiles("test-no-db")
public class SearchControllerTest {

    @MockBean
    private SearchService service;

    @MockBean
    private ImageRepo repo;

    @MockBean
    private SearchQueryEventValidator validator;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testDoSearch_whenValidRequest_thenHttp200() {
        final String query = "name";
        final int totalResults = 1;
        final long id = 1L;
        final String url = "http://image.png";
        final String title = "image";
        final String description = "descr";
        final int duration = 5;
        final String slideshowName = "slideshow";

        final SearchResponse searchResult = SearchResponse.builder()
                .query(query)
                .totalResults(totalResults)
                .images(List.of(
                        ImageResponse.builder()
                                .id(id)
                                .url(url)
                                .title(title)
                                .description(description)
                                .duration(duration)
                                .slideshow(SlideshowResponse.builder()
                                        .id(id)
                                        .name(slideshowName)
                                        .build())
                                .build()
                ))
                .build();
        doNothing().when(validator).validate(query);
        Mockito.when(service.search(query)).thenReturn(Mono.just(searchResult));

        webClient.get()
                .uri("/api/v1/image?query=" + query)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBody()
                .json(new JSONObject(
                        Map.of(
                                "query", query,
                                "totalResults", totalResults,
                                "images", List.of(
                                        Map.of(
                                                "id", id,
                                                "url", url,
                                                "title", title,
                                                "description", description,
                                                "duration", duration,
                                                "slideshow", Map.of(
                                                        "id", id,
                                                        "name", slideshowName
                                                )
                                        )
                                )
                        )
                ).toString());

        Mockito.verify(validator, times(totalResults)).validate(query);
        Mockito.verify(service, times(totalResults)).search(query);
    }


    @Test
    public void testDoSearch_whenInValidRequest_thenBadRequest() {

        webClient.get()
                .uri("/api/v1/image?query")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);

        Mockito.verifyNoInteractions(validator);
        Mockito.verifyNoInteractions(service);
    }
}
