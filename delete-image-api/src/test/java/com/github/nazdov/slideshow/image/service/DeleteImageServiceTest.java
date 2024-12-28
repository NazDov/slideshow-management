package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.config.AbstractTestContainersIntegrationTest;
import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
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
public class DeleteImageServiceTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    private DeleteImageService service;

    @Autowired
    private ImageRepo repo;

    @Test
    public void testDeleteImage_isDeleted() {
        // save data for testing
        Image created = repo.save(Image.builder()
                        .url("http://test")
                        .title("test")
                        .description("test")
                        .duration(5L)
                        .createdAt(LocalDateTime.now())
                        .build())
                .block();

        assertNotNull(Objects.requireNonNull(created).getId());
        assertFalse(Objects.requireNonNull(created).isDeleted());

        //verify deletion completed
        StepVerifier.create(service.delete(created.getId()))
                .assertNext(image -> {
                    assertTrue(image.isDeleted());
                })
                .verifyComplete();

    }

}
