package com.github.nazdov.slideshow.service;

import com.github.nazdov.slideshow.image.core.dto.SlideshowEvent;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.producer.SlideshowKafkaEventProducer;
import com.github.nazdov.slideshow.image.core.repository.ImageRepo;
import com.github.nazdov.slideshow.image.core.util.SerializationUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@Slf4j
public class ProofOfPlayService {

    private final SlideshowKafkaEventProducer producer;
    private final ImageRepo repo;

    public ProofOfPlayService(ImageRepo repo,
                              SlideshowKafkaEventProducer producer) {
        this.producer = producer;
        this.repo = repo;
    }

    public Mono<Void> recordProofOfPlay(Tuple2<Long, Long> slideshowAndImage) {

        return repo
                .findByIdAndSlideshowId(slideshowAndImage.getT1(), slideshowAndImage.getT2())
                .switchIfEmpty(
                        Mono.error(
                                new ValidationException(
                                        "No image found for an associated slideshow by id: " + slideshowAndImage.getT2())))
                .map(image -> SlideshowEvent.builder()
                        .topic(producer.getSlideshowEventTopicName())
                        .key(image.getId().toString())
                        .value(SerializationUtils.serializeObjectToJson(image))
                        .build())
                .flatMap(producer::produce)
                .then();
    }
}
