package com.github.nazdov.slideshow.image.config;


import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.service.SearchService;
import com.github.nazdov.slideshow.image.validation.SearchQueryEventValidator;
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
public class SearchConfig {

    @Bean
    public SearchQueryEventValidator searchQueryEventValidator() {
        return new SearchQueryEventValidator();
    }

    @Bean
    public SearchService searchService(ImageRepo imageRepo) {
        return new SearchService(imageRepo);
    }
}
