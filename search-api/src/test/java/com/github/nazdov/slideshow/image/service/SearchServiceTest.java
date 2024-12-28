package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.config.AbstractTestContainersIntegrationTest;
import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.image.dto.ImageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataR2dbcTest
@ActiveProfiles("test")
@Import(value = {TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SearchServiceTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    private SearchService service;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private SlideshowRepo slideshowRepo;

    @Test
    public void testQuerySearch_whenHasImages_returnSuccess() {
        // save data for testing
        Slideshow createdSlideshow = slideshowRepo.save(
                        Slideshow
                                .builder()
                                .name("test slideshow")
                                .createdAt(LocalDateTime.now())
                                .build())
                .block();

        List<Image> createdImages = imageRepo.saveAll(List.of(
                        Image
                                .builder()
                                .url("https://i.imgur.com/base1.jpg")
                                .title("base 1")
                                .description("some description 1")
                                .createdAt(LocalDateTime.now())
                                .slideshowId(Objects.requireNonNull(createdSlideshow).getId())
                                .duration(5L)
                                .build(),

                        Image
                                .builder()
                                .url("https://i.imgur.com/base2.jpg")
                                .title("base 2")
                                .description("some description 2")
                                .createdAt(LocalDateTime.now())
                                .slideshowId(Objects.requireNonNull(createdSlideshow).getId())
                                .duration(5L).build()))
                .collectList()
                .block();

        assertNotNull(Objects.requireNonNull(createdSlideshow).getId());

        //verify search query completed
        String query = "base";

        StepVerifier.create(service.search(query))
                .assertNext(searchResponse -> {
                    assertEquals(query, searchResponse.getQuery());
                    assertEquals(Objects.requireNonNull(createdImages).size(),
                            searchResponse.getTotalResults());
                    List<ImageResponse> responseImages = searchResponse.getImages();
                    IntStream.range(0, responseImages.size())
                            .forEach(i -> {
                                assertEquals(createdImages.get(i).getId(), responseImages.get(i).getId());
                                assertEquals(createdImages.get(i).getUrl(), responseImages.get(i).getUrl());
                                assertEquals(createdImages.get(i).getTitle(), responseImages.get(i).getTitle());
                                assertEquals(createdImages.get(i).getDescription(), responseImages.get(i).getDescription());
                                assertEquals(createdImages.get(i).getDuration(), responseImages.get(i).getDuration());
                                assertEquals(createdImages.get(i).getSlideshowId(), responseImages.get(i).getSlideshow().getId());

                            });
                })
                .verifyComplete();

    }


    @Test
    public void testQuerySearch_whenNoResults_returnError() {

        final String query = "base";
        StepVerifier.create(service.search(query))
                .expectErrorMessage("Images not found by search query: " + query)
                .verify();

    }

}
