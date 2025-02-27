package com.github.nazdov.slideshow.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class DeleteImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeleteImageApplication.class, args);
    }
}
