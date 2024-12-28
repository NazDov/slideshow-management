package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.config.AbstractTestContainersIntegrationTest;
import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.entity.Slideshow;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.service.DeleteSlideshowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataR2dbcTest
@ActiveProfiles("test")
@Import(value = {TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteSlideshowServiceTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    private DeleteSlideshowService service;

    @Autowired
    private SlideshowRepo repo;

    @Test
    public void testDeleteSlideshow_isDeleted() {
        // save data for testing
        Slideshow created = repo.save(
                        Slideshow.builder()
                                .name("test")
                                .createdAt(LocalDateTime.now())
                                .build()
                )
                .block();

        assertNotNull(Objects.requireNonNull(created).getId());
        assertFalse(Objects.requireNonNull(created).isDeleted());

        //verify deletion completed
        StepVerifier.create(service.delete(created.getId()))
                .assertNext(slideshow -> {
                    assertTrue(slideshow.isDeleted());
                })
                .verifyComplete();

    }

}
