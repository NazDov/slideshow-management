package com.github.nazdov.slideshow.image.controller;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.dto.AddImageRequest;
import com.github.nazdov.slideshow.image.mapper.AddImageRequestMapper;
import com.github.nazdov.slideshow.image.service.AddImageService;
import com.github.nazdov.slideshow.image.validation.AddImageEventValidator;
import org.json.JSONObject;
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

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = AddImageController.class)
@ExtendWith(SpringExtension.class)
@Import({TestConfig.class})
@ActiveProfiles("test-no-db")
public class AddImageControllerTest {

    @MockBean
    private AddImageService service;

    @MockBean
    private ImageRepo repo;

    @MockBean
    private AddImageEventValidator validator;

    @MockBean
    private AddImageRequestMapper requestMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testAddImage_whenValidRequest_thenHttp201() {
        AddImageRequest request = AddImageRequest.builder()
                .url("https://i.imgur.com/CzXTtJV.jpg")
                .title("some title")
                .description("some description")
                .duration(5L)
                .build();

        doNothing().when(validator).validate(request);
        Image image = Image.builder()
                .id(100L)
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(requestMapper.mapFrom(request)).thenReturn(Mono.just(image));
        Mockito.when(service.add(any())).thenReturn(
                Mono.just(image)
        );

        webClient.post()
                .uri("/api/v1/image")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isCreated();

        Mockito.verify(validator, times(1)).validate(request);
        Mockito.verify(service, times(1)).add(image);
    }

    @Test
    public void testAddImage_whenInValidRequest_thenHttp400() {
        final AddImageRequest request = AddImageRequest.builder()
                .url("CzXTtJV")
                .title("some title")
                .description("some description")
                .duration(5L)
                .build();

        final String expectedErrorMessage = "Not an image";
        doThrow(new ValidationException(expectedErrorMessage)).when(validator).validate(request);

        webClient.post()
                .uri("/api/v1/image")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .json(new JSONObject(
                        Map
                                .of("message", expectedErrorMessage))
                        .toString());

        Mockito.verify(validator, times(1)).validate(request);
        Mockito.verifyNoInteractions(service);
    }

}
