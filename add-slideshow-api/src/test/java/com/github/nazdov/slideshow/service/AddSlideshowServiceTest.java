package com.github.nazdov.slideshow.service;

import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.config.AbstractTestContainersIntegrationTest;
import com.github.nazdov.slideshow.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
@DataR2dbcTest
@ActiveProfiles("test")
@Import(value = {TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddSlideshowServiceTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    private AddSlideshowService service;

    @Autowired
    private SlideshowRepo slRepo;

    @Autowired
    private ImageRepo imRepo;

    @Test
    public void testAddSlideshow_withValidUrls_Created() {

        //create test images
        imRepo.saveAll(List.of(
                        Image
                                .builder()
                                .url("https://i.imgur.com/base1.jpg")
                                .title("base 1")
                                .description("some description 1")
                                .createdAt(LocalDateTime.now())
                                .duration(5L)
                                .build(),

                        Image
                                .builder()
                                .url("https://i.imgur.com/base2.jpg")
                                .title("base 2")
                                .description("some description 2")
                                .createdAt(LocalDateTime.now())
                                .duration(5L).build()))
                .collectList()
                .block();

        //create slideshow with associated image urls
        AtomicLong slideshowId = new AtomicLong();
        StepVerifier.create(service.add(Tuples.of(
                Slideshow
                        .builder()
                        .name("test slideshow")
                        .createdAt(LocalDateTime.now()).build(),
                List.of(),
                Optional.of(
                        List
                                .of("https://i.imgur.com/base1.jpg", "https://i.imgur.com/base2.jpg"

                                )).orElseGet(List::of)))).assertNext(slideshow -> {
            slideshowId.set(slideshow.getId());
            assertNotNull(slideshow.getId());
            assertNotNull(slideshow.getCreatedAt());

        }).verifyComplete();


        //verify images have the slideshowId
        StepVerifier.create(imRepo.findAllByUrls(
                List
                        .of("https://i.imgur.com/base1.jpg", "https://i.imgur.com/base2.jpg"

                        )).collectList()).assertNext(images -> {
            assertTrue(images.stream().allMatch(i -> Objects.equals(i.getSlideshowId(), slideshowId.get())));
        }).verifyComplete();
    }

    @Test
    public void testAddSlideshow_withValidIds_Created() {

        //create test images
        List<Image> createdImages = imRepo.saveAll(
                        List.of(Image.builder()
                                        .url("https://i.imgur.com/base3.jpg")
                                        .title("base 3")
                                        .description("some description 3")
                                        .createdAt(LocalDateTime.now())
                                        .duration(5L)
                                        .build(),

                                Image.builder()
                                        .url("https://i.imgur.com/base4.jpg")
                                        .title("base 4")
                                        .description("some description 4")
                                        .createdAt(LocalDateTime.now())
                                        .duration(5L)
                                        .build()))
                .collectList()
                .block();

        //create slideshow with associated image ids
        AtomicLong slideshowId = new AtomicLong();
        StepVerifier.create(service.add(Tuples.of(
                Slideshow.builder()
                        .name("test slideshow")
                        .createdAt(LocalDateTime.now())
                        .build(),
                Objects.requireNonNull(createdImages).stream().map(Image::getId).collect(Collectors.toList()),
                List.of()))

        ).assertNext(slideshow -> {
            slideshowId.set(slideshow.getId());
            assertNotNull(slideshow.getId());
            assertNotNull(slideshow.getCreatedAt());

        }).verifyComplete();

        //verify images have the slideshowId
        StepVerifier.create(imRepo.findAll().collectList()).assertNext(images -> {
            assertTrue(images.stream().allMatch(i -> Objects.equals(i.getSlideshowId(), slideshowId.get())));
        }).verifyComplete();
    }


    @Test
    public void testAddSlideshow_whenImagesNotFound_ExpectError() {

        StepVerifier.create(service.add(Tuples.of(
                        Slideshow
                                .builder()
                                .name("test slideshow")
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Objects.requireNonNull(List.of(120L, 121L)),
                        List.of()))
                )
                .expectErrorMessage("No images found by either Ids or Urls.")
                .verify();
    }

}
