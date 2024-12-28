package com.github.nazdov.slideshow;


import com.github.nazdov.slideshow.config.AddSlideshowConfig;
import com.github.nazdov.slideshow.config.DeleteSlideshowConfig;
import com.github.nazdov.slideshow.config.ProofOfPlayConfig;
import com.github.nazdov.slideshow.config.SlideshowOrderConfig;
import com.github.nazdov.slideshow.image.config.AddImageConfig;
import com.github.nazdov.slideshow.image.config.DeleteImageConfig;
import com.github.nazdov.slideshow.image.config.SearchConfig;
import com.github.nazdov.slideshow.image.core.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@OpenAPIDefinition(
        info = @Info(
                title = "Slideshow Management Application",
                version = "1.0",
                description = "The Slideshow Management API provides endpoints for" +
                        " managing slideshows and images, including CRUD operations" +
                        " for slides, slideshows, and associated metadata." +
                        " It enables functionalities such as adding/removing images" +
                        " and slideshows, searching for slideshows, and managing slideshow" +
                        " orders and proof of play events logging"
        ))
@SpringBootApplication
@Import(value = {
        SwaggerConfig.class,
        AddImageConfig.class,
        DeleteImageConfig.class,
        AddSlideshowConfig.class,
        DeleteSlideshowConfig.class,
        SearchConfig.class,
        SlideshowOrderConfig.class,
        ProofOfPlayConfig.class
})
@EnableR2dbcRepositories
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
