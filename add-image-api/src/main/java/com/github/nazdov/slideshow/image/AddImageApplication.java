package com.github.nazdov.slideshow.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class AddImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddImageApplication.class, args);
    }
}
