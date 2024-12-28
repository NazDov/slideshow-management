package com.github.nazdov.slideshow.image.config;


import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.mapper.AddImageResponseMapper;
import com.github.nazdov.slideshow.image.service.AddImageService;
import com.github.nazdov.slideshow.image.validation.AddImageEventValidator;
import com.github.nazdov.slideshow.image.mapper.AddImageRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        DatabaseConfig.class
})
@Slf4j
public class AddImageConfig {

    @Bean
    public AddImageEventValidator imageEventValidator() {
        return new AddImageEventValidator();
    }

    @Bean
    public AddImageService addImageService(ImageRepo imageRepo) {
        return new AddImageService(imageRepo);
    }

    @Bean
    public AddImageRequestMapper addImageRequestMapper() {
        return new AddImageRequestMapper();
    }

    @Bean
    public AddImageResponseMapper addImageResponseMapper() {
        return new AddImageResponseMapper();
    }
}
