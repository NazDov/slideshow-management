package com.github.nazdov.slideshow.image.config;


import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.service.DeleteImageService;
import com.github.nazdov.slideshow.image.validation.DeleteImageEventValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@Import(value = {
        DatabaseConfig.class
})
@Slf4j
public class DeleteImageConfig {

    @Bean
    public DeleteImageEventValidator deleteImageEventValidator() {
        return new DeleteImageEventValidator();
    }

    @Bean
    public DeleteImageService deleteImageService(ImageRepo imageRepo) {
        return new DeleteImageService(imageRepo);
    }
}
