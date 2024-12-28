package com.github.nazdov.slideshow.config;


import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.service.DeleteSlideshowService;
import com.github.nazdov.slideshow.validation.DeleteSlideshowEventValidator;
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
public class DeleteSlideshowConfig {

    @Bean
    public DeleteSlideshowEventValidator deleteSlideshowEventValidator() {
        return new DeleteSlideshowEventValidator();
    }

    @Bean
    public DeleteSlideshowService deleteSlideshowService(SlideshowRepo slideshowRepo) {
        return new DeleteSlideshowService(slideshowRepo);
    }
}
