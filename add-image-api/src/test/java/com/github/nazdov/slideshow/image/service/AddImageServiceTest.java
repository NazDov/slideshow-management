package com.github.nazdov.slideshow.image.service;

import com.github.nazdov.slideshow.image.config.AbstractTestContainersIntegrationTest;
import com.github.nazdov.slideshow.image.config.TestConfig;
import com.github.nazdov.slideshow.image.core.entity.Image;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@DataR2dbcTest
@ActiveProfiles("test")
@Import(value = {TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddImageServiceTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    @Qualifier("addImageService")
    private AddImageService service;

    @Autowired
    private ImageRepo repo;

    @Test
    public void testAddImage_Created() {

        StepVerifier.create(service.add(
                        Image.builder()
                                .url("https://i.imgur.com/base.jpg")
                                .title("some title")
                                .description("some description")
                                .createdAt(LocalDateTime.now())
                                .duration(5L)
                                .build()))
                .assertNext(createdImage -> {
                    assertNotNull(createdImage.getId());
                    assertNotNull(createdImage.getCreatedAt());
                })
                .verifyComplete();
    }

}
