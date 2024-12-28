package com.github.nazdov.slideshow.controller;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.dto.AddSlideshowRequest;
import com.github.nazdov.slideshow.config.TestConfig;
import com.github.nazdov.slideshow.service.AddSlideshowService;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.mapper.AddSlideshowRequestMapper;
import com.github.nazdov.slideshow.validation.AddSlideshowEventValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = AddSlideshowController.class)
@ExtendWith(SpringExtension.class)
@Import({TestConfig.class})
@ActiveProfiles("test-no-db")
public class AddSlideshowControllerTest {

    @MockBean
    private AddSlideshowService service;

    @MockBean
    private ImageRepo repo;

    @MockBean
    private AddSlideshowEventValidator validator;

    @MockBean
    private AddSlideshowRequestMapper requestMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testAddSlideshow_whenValidRequestWithImageIds_thenHttp201() {
        AddSlideshowRequest request = AddSlideshowRequest.builder()
                .name("slideshow title")
                .imageIds(List.of(1L, 2L))
                .build();

        doNothing().when(validator).validate(request);

        Slideshow slideshow = Slideshow.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Tuple3<Slideshow, List<Long>, List<String>> data = Tuples.of(
                slideshow,
                Optional.ofNullable(request.getImageIds()).orElseGet(List::of),
                Optional.ofNullable(request.getImageUrls()).orElseGet(List::of)
        );

        Mockito.when(requestMapper.mapFrom(request)).thenReturn(Mono.just(data));
        Mockito.when(service.add(data)).thenReturn(
                Mono.just(slideshow.copyFrom(slideshow).id(1L).build())
        );

        webClient.post()
                .uri("/api/v1/slideshow")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isCreated();

        Mockito.verify(validator, times(1)).validate(request);
        Mockito.verify(service, times(1)).add(data);
    }

    @Test
    public void testAddSlideshow_whenValidRequestWithUrls_thenHttp201() {
        AddSlideshowRequest request = AddSlideshowRequest.builder()
                .name("slideshow title")
                .imageUrls(List.of(
                        "https://i.imgur.com/OnwEDW3.jpg",
                        "https://farm6.staticflickr.com/5590/14821526429_5c6ea60405_z_d.jpg"
                ))
                .build();

        doNothing().when(validator).validate(request);

        Slideshow slideshow = Slideshow.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Tuple3<Slideshow, List<Long>, List<String>> data = Tuples.of(
                slideshow,
                Optional.ofNullable(request.getImageIds()).orElseGet(List::of),
                Optional.ofNullable(request.getImageUrls()).orElseGet(List::of)
        );

        Mockito.when(requestMapper.mapFrom(request)).thenReturn(Mono.just(data));
        Mockito.when(service.add(data)).thenReturn(
                Mono.just(slideshow.copyFrom(slideshow).id(1L).build())
        );

        webClient.post()
                .uri("/api/v1/slideshow")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isCreated();

        Mockito.verify(validator, times(1)).validate(request);
        Mockito.verify(service, times(1)).add(data);
    }

    @Test
    public void testAddSlideshow_whenInValidRequest_thenHttp400() {
        final AddSlideshowRequest request = AddSlideshowRequest.builder()
                .name("slideshow title")
                .build();

        final String expectedErrorMessage = "Missing imageIds or imageUrls";
        doThrow(new ValidationException(expectedErrorMessage)).when(validator).validate(request);

        webClient.post()
                .uri("/api/v1/slideshow")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();

        Mockito.verify(validator, times(1)).validate(request);
        Mockito.verifyNoInteractions(service);
    }

}
