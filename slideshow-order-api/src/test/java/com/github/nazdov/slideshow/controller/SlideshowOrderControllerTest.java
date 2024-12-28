package com.github.nazdov.slideshow.controller;

import com.github.nazdov.slideshow.config.TestConfig;
import com.github.nazdov.slideshow.dto.SlideshowImageResponse;
import com.github.nazdov.slideshow.dto.SlideshowOrderResponse;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.service.SlideshowOrderService;
import com.github.nazdov.slideshow.validation.SlideshowOrderEventValidator;
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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = SlideshowOrderController.class)
@ExtendWith(SpringExtension.class)
@Import({TestConfig.class})
@ActiveProfiles("test-no-db")
public class SlideshowOrderControllerTest {

    private final static DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");
    @MockBean
    private SlideshowOrderService service;

    @MockBean
    private ImageRepo repo;

    @MockBean
    private SlideshowOrderEventValidator validator;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testSearchSlideshowOrder_whenValidRequestAndOrderASC_thenHttp200() {
        Tuple2<String, String> query = Tuples.of("1", "asc");

        String slideshowName = "test";
        Long id1 = 1L;
        Long id2 = 2L;
        String url = "http://test";
        String title = "title";
        Integer duration = 5;

        SlideshowOrderResponse searchResult = buildRequest(
                slideshowName, id1, id2, url, title, duration);

        doNothing().when(validator).validate(query);
        Mockito.when(service.search(query)).thenReturn(Mono.just(searchResult));

        webClient.get()
                .uri("/api/v1/slideshow/" + query.getT1() + "?order=" + query.getT2())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBody()
                .json(new JSONObject(
                        Map.of(
                                "name", slideshowName,
                                "images", List.of(
                                        Map.of(
                                                "id", id1,
                                                "url", url,
                                                "title", title,
                                                "duration", duration,
                                                "createdAt", formatter.format(LocalDateTime.of(
                                                        2024,
                                                        12,
                                                        1,
                                                        21,
                                                        10,
                                                        10
                                                ))

                                        ),

                                        Map.of(
                                                "id", id2,
                                                "url", url,
                                                "title", title,
                                                "duration", duration,
                                                "createdAt", formatter.format(LocalDateTime.of(
                                                        2024,
                                                        12,
                                                        2,
                                                        21,
                                                        10,
                                                        10
                                                ))
                                        )
                                )
                        )
                ).toString(), false);

        Mockito.verify(validator, times(1)).validate(query);
        Mockito.verify(service, times(1)).search(query);
    }


    @Test
    public void testSearchSlideshowOrder_whenValidRequestAndOrderDESC_thenHttp200() {
        Tuple2<String, String> query = Tuples.of("1", "desc");

        String slideshowName = "test";
        Long id1 = 1L;
        Long id2 = 2L;
        String url = "http://test";
        String title = "title";
        Integer duration = 5;

        SlideshowOrderResponse searchResult = buildRequest(
                slideshowName, id1, id2, url, title, duration);

        doNothing().when(validator).validate(query);
        Mockito.when(service.search(query)).thenReturn(Mono.just(searchResult));

        webClient.get()
                .uri("/api/v1/slideshow/" + query.getT1() + "?order=" + query.getT2())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBody()
                .json(new JSONObject(
                        Map.of(
                                "name", slideshowName,
                                "images", List.of(

                                        Map.of(
                                                "id", id2,
                                                "url", url,
                                                "title", title,
                                                "duration", duration,
                                                "createdAt", formatter.format(LocalDateTime.of(
                                                        2024,
                                                        12,
                                                        2,
                                                        21,
                                                        10,
                                                        10
                                                ))
                                        ),

                                        Map.of(
                                                "id", id1,
                                                "url", url,
                                                "title", title,
                                                "duration", duration,
                                                "createdAt", formatter.format(LocalDateTime.of(
                                                        2024,
                                                        12,
                                                        1,
                                                        21,
                                                        10,
                                                        10
                                                ))

                                        )
                                )
                        )
                ).toString(), false);

        Mockito.verify(validator, times(1)).validate(query);
        Mockito.verify(service, times(1)).search(query);
    }

    private SlideshowOrderResponse buildRequest(String slideshowName, Long id1, Long id2, String url, String title, Integer duration) {
        return SlideshowOrderResponse.builder()
                .name(slideshowName)
                .images(List.of(
                        SlideshowImageResponse.builder()
                                .id(id1)
                                .url(url)
                                .title(title)
                                .duration(duration)
                                .createdAt(LocalDateTime.of(
                                        2024,
                                        12,
                                        1,
                                        21,
                                        10,
                                        10
                                ))
                                .build(),

                        SlideshowImageResponse.builder()
                                .id(id2)
                                .url(url)
                                .title(title)
                                .duration(duration)
                                .createdAt(LocalDateTime.of(
                                        2024,
                                        12,
                                        2,
                                        21,
                                        10,
                                        10
                                ))
                                .build()
                ))
                .build();
    }

    @Test
    public void testSearchSlideshowOrder_whenInValidQuery_thenBadRequest() {

        Tuple2<String, String> query = Tuples.of("1", "test");
        doThrow(
                new ValidationException("invalid or missing order parameter. supported: ASC, DESC"))
                .when(validator).validate(query);

        webClient.get()
                .uri("/api/v1/slideshow/" + query.getT1() + "?order=" + query.getT2())
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .json(new JSONObject(
                        Map
                                .of("message", "invalid or missing order parameter. supported: ASC, DESC"))
                        .toString());

        Mockito.verify(validator, times(1)).validate(query);
        Mockito.verifyNoInteractions(service);
    }
}
