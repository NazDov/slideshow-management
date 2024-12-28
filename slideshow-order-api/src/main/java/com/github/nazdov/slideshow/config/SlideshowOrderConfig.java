package com.github.nazdov.slideshow.config;


import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.service.SlideshowOrderService;
import com.github.nazdov.slideshow.validation.SlideshowOrderEventValidator;
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
public class SlideshowOrderConfig {

    @Bean
    public SlideshowOrderService slideshowOrderService(SlideshowRepo slideshowRepo) {
        return new SlideshowOrderService(slideshowRepo);
    }

    @Bean
    public SlideshowOrderEventValidator slideshowOrderEventValidator() {
        return new SlideshowOrderEventValidator();
    }
}
