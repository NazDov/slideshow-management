package com.github.nazdov.slideshow.image.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Slideshow Management Application")
                                .version("1.0")
                                .description("The Slideshow Management API provides endpoints for" +
                                        " managing slideshows and images, including CRUD operations" +
                                        " for slides, slideshows, and associated metadata." +
                                        " It enables functionalities such as adding/removing images" +
                                        " and slideshows, searching for slideshows, and managing slideshow" +
                                        " orders and proof of play events logging.")
                );
    }
}
