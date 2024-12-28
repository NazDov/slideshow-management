package com.github.nazdov.slideshow.service;

import com.github.nazdov.slideshow.config.AbstractTestContainersIntegrationTest;
import com.github.nazdov.slideshow.config.TestConfig;
import com.github.nazdov.slideshow.dto.SlideshowImageResponse;
import com.github.nazdov.slideshow.dto.SlideshowOrder;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
@DataR2dbcTest
@ActiveProfiles("test")
@Import(value = {TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SlideshowOrderServiceTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    private SlideshowOrderService service;

    @Autowired
    private SlideshowRepo slideshowRepo;

    @Autowired
    private ImageRepo imageRepo;

    @Test
    public void testSearch_whenOrderAscandHasSlideshow_returnSuccess() {
        // save data for testing
        Slideshow createdSlideshow = slideshowRepo.save(
                        Slideshow
                                .builder()
                                .name("test slideshow")
                                .createdAt(LocalDateTime.now())
                                .build())
                .block();

        //create images in DESC order
        List<Image> createdImages = imageRepo.saveAll(List.of(
                        Image
                                .builder()
                                .url("https://i.imgur.com/base1Asc.jpg")
                                .title("base 1")
                                .description("some description 1")
                                .createdAt(LocalDateTime.of(
                                        2024,
                                        12,
                                        1,
                                        21,
                                        10,
                                        10
                                ))
                                .slideshowId(Objects.requireNonNull(createdSlideshow).getId())
                                .duration(5L)
                                .build(),

                        Image
                                .builder()
                                .url("https://i.imgur.com/base2Asc.jpg")
                                .title("base 2")
                                .description("some description 2")
                                .createdAt(LocalDateTime.of(
                                        2024,
                                        12,
                                        2,
                                        21,
                                        10,
                                        10
                                ))
                                .slideshowId(Objects.requireNonNull(createdSlideshow).getId())
                                .duration(5L).build()))
                .collectList()
                .block();

        assertNotNull(Objects.requireNonNull(createdSlideshow).getId());


        Tuple2<String, String> query = Tuples.of(
                createdSlideshow.getId().toString(),
                SlideshowOrder.ASC.name());


        StepVerifier.create(service.search(query))
                .assertNext(slideshowOrderResponse -> {
                    assertEquals(createdSlideshow.getName(), slideshowOrderResponse.getName());
                    assertEquals(createdSlideshow.getCreatedAt().truncatedTo(ChronoUnit.HOURS),
                            slideshowOrderResponse.getCreatedAt().truncatedTo(ChronoUnit.HOURS));

                    assertEquals(Objects.requireNonNull(createdImages).size(),
                            slideshowOrderResponse.getImages().size());

                    List<SlideshowImageResponse> responseImages = slideshowOrderResponse.getImages();
                    IntStream.range(0, responseImages.size())
                            .forEach(i -> {
                                assertEquals(createdImages.get(i).getId(), responseImages.get(i).getId());
                                assertEquals(createdImages.get(i).getUrl(), responseImages.get(i).getUrl());
                                assertEquals(createdImages.get(i).getTitle(), responseImages.get(i).getTitle());
                                assertEquals(createdImages.get(i).getDescription(), responseImages.get(i).getDescription());
                                assertEquals(createdImages.get(i).getDuration().longValue(), responseImages.get(i).getDuration().longValue());
                                assertEquals(createdImages.get(i).getCreatedAt()
                                                .truncatedTo(ChronoUnit.MINUTES),
                                        responseImages.get(i).getCreatedAt()
                                                .truncatedTo(ChronoUnit.MINUTES));

                            });
                })
                .verifyComplete();
    }


    @Test
    public void testSearch_whenOrderDescandHasSlideshow_returnSuccess() {
        // save data for testing
        Slideshow createdSlideshow = slideshowRepo.save(
                        Slideshow
                                .builder()
                                .name("test slideshow")
                                .createdAt(LocalDateTime.now())
                                .build())
                .block();

        //create images in DESC order
        List<Image> createdImages = imageRepo.saveAll(List.of(
                        Image
                                .builder()
                                .url("https://i.imgur.com/base2Desc.jpg")
                                .title("base 2")
                                .description("some description 2")
                                .createdAt(LocalDateTime.of(
                                        2024,
                                        12,
                                        2,
                                        21,
                                        10,
                                        10
                                ))
                                .slideshowId(Objects.requireNonNull(createdSlideshow).getId())
                                .duration(5L)
                                .build(),

                        Image
                                .builder()
                                .url("https://i.imgur.com/base1Desc.jpg")
                                .title("base 1")
                                .description("some description 1")
                                .createdAt(LocalDateTime.of(
                                        2024,
                                        12,
                                        1,
                                        21,
                                        10,
                                        10
                                ))
                                .slideshowId(Objects.requireNonNull(createdSlideshow).getId())
                                .duration(5L).build()))
                .collectList()
                .block();

        assertNotNull(Objects.requireNonNull(createdSlideshow).getId());


        Tuple2<String, String> query = Tuples.of(
                createdSlideshow.getId().toString(),
                SlideshowOrder.DESC.name());


        StepVerifier.create(service.search(query))
                .assertNext(slideshowOrderResponse -> {
                    assertEquals(createdSlideshow.getName(), slideshowOrderResponse.getName());
                    assertEquals(createdSlideshow.getCreatedAt().truncatedTo(ChronoUnit.HOURS),
                            slideshowOrderResponse.getCreatedAt().truncatedTo(ChronoUnit.HOURS));

                    assertEquals(Objects.requireNonNull(createdImages).size(),
                            slideshowOrderResponse.getImages().size());

                    List<SlideshowImageResponse> responseImages = slideshowOrderResponse.getImages();
                    IntStream.range(0, responseImages.size())
                            .forEach(i -> {
                                assertEquals(createdImages.get(i).getId(), responseImages.get(i).getId());
                                assertEquals(createdImages.get(i).getUrl(), responseImages.get(i).getUrl());
                                assertEquals(createdImages.get(i).getTitle(), responseImages.get(i).getTitle());
                                assertEquals(createdImages.get(i).getDescription(), responseImages.get(i).getDescription());
                                assertEquals(createdImages.get(i).getDuration().longValue(), responseImages.get(i).getDuration().longValue());
                                assertEquals(createdImages.get(i).getCreatedAt()
                                                .truncatedTo(ChronoUnit.MINUTES),
                                        responseImages.get(i).getCreatedAt()
                                                .truncatedTo(ChronoUnit.MINUTES));

                            });
                })
                .verifyComplete();
    }

}
