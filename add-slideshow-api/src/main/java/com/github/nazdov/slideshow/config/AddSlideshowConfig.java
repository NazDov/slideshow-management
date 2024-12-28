package com.github.nazdov.slideshow.config;

import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.mapper.AddSlideshowRequestMapper;
import com.github.nazdov.slideshow.mapper.AddSlideshowResponseMapper;
import com.github.nazdov.slideshow.service.AddSlideshowService;
import com.github.nazdov.slideshow.validation.AddSlideshowEventValidator;
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
public class AddSlideshowConfig {

    @Bean
    public AddSlideshowEventValidator slideshowEventValidator() {
        return new AddSlideshowEventValidator();
    }

    @Bean
    public AddSlideshowService addSlideshowService(SlideshowRepo slideshowRepo, ImageRepo imageRepo) {
        return new AddSlideshowService(slideshowRepo, imageRepo);
    }

    @Bean
    public AddSlideshowRequestMapper addSlideshowRequestMapper() {
        return new AddSlideshowRequestMapper();
    }

    @Bean
    public AddSlideshowResponseMapper addSlideshowResponseMapper() {
        return new AddSlideshowResponseMapper();
    }
}

