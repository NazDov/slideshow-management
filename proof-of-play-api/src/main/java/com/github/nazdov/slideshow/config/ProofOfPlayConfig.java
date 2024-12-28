package com.github.nazdov.slideshow.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.nazdov.slideshow.image.core.config.DatabaseConfig;
import com.github.nazdov.slideshow.image.core.config.SlideshowKafkaProducerConfig;
import com.github.nazdov.slideshow.image.core.producer.SlideshowKafkaEventProducer;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.repository.SlideshowRepo;
import com.github.nazdov.slideshow.service.ProofOfPlayService;
import com.github.nazdov.slideshow.validation.ProofOfPlayEventValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@Import(value = {
        DatabaseConfig.class,
        SlideshowKafkaProducerConfig.class
})
@Slf4j
public class ProofOfPlayConfig {

    @Bean
    public ProofOfPlayService proofOfPlayService(ImageRepo repo, SlideshowKafkaEventProducer producer) {
        return new ProofOfPlayService(repo, producer);
    }

    @Bean
    public ProofOfPlayEventValidator proofOfPlayEventValidator() {
        return new ProofOfPlayEventValidator();
    }
}
