package com.github.nazdov.slideshow.image.core.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
public class SlideshowKafkaEventConsumer implements ReactiveEventConsumer<String> {
    private final ReactiveKafkaConsumerTemplate<String, String> consumerTemplate;

    public SlideshowKafkaEventConsumer(ReactiveKafkaConsumerTemplate<String, String> consumerTemplate) {
        this.consumerTemplate = consumerTemplate;
    }

    @Override
    public Flux<String> consume(String topic) {
        // Subscribe to the topic and receive messages reactively
        return consumerTemplate.receive()
                .doOnNext(record -> log.info("Received: {}", record.value()))
                .map(ReceiverRecord::value)
                .doOnError(e -> log.info("Error consuming message: {}", e.getMessage()));
    }
}
