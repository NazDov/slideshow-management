package com.github.nazdov.slideshow.image.controller;

import com.github.nazdov.slideshow.controller.DeleteSlideshowController;
import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.service.DeleteSlideshowService;
import com.github.nazdov.slideshow.validation.DeleteSlideshowEventValidator;
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

import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@WebFluxTest(controllers = DeleteSlideshowController.class)
@ExtendWith(SpringExtension.class)
@Import({TestConfig.class})
@ActiveProfiles("test-no-db")
public class DeleteSlideshowControllerTest {

    @MockBean
    private DeleteSlideshowService service;

    @MockBean
    private ImageRepo repo;

    @MockBean
    private DeleteSlideshowEventValidator validator;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testDeleteSlideshow_whenValidRequest_thenHttp204() {
        final Long deleteImageId = 1L;

        doNothing().when(validator).validate(deleteImageId);
        Mockito.when(service.delete(deleteImageId)).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/v1/slideshow/1")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NO_CONTENT);

        Mockito.verify(validator, times(1)).validate(deleteImageId);
        Mockito.verify(service, times(1)).delete(deleteImageId);
    }

    @Test
    public void testDeleteSlideshow_whenInValidImage_thenBadRequest() {

        long notExistingImageId = 100500L;
        doNothing().when(validator).validate(notExistingImageId);
        Mockito.when(service.delete(notExistingImageId))
                .thenReturn(Mono.error(new EntityNotFoundException("Entity not found, id: " + notExistingImageId)));

        webClient.delete()
                .uri("/api/v1/slideshow/" + notExistingImageId)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .json(new JSONObject(
                        Map
                                .of("message", "Entity not found, id: " + notExistingImageId))
                        .toString());

        Mockito.verify(validator, times(1)).validate(notExistingImageId);
        Mockito.verify(service, times(1)).delete(notExistingImageId);
    }

    @Test
    public void testDeleteSlideshow_whenInValidRequest_thenBadRequest() {

        webClient.delete()
                .uri("/api/v1/slideshow/null")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);

        Mockito.verifyNoInteractions(validator);
        Mockito.verifyNoInteractions(service);
    }
}
