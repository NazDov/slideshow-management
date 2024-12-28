package com.github.nazdov.slideshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class SlideshowOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlideshowOrderApplication.class, args);
    }
}
