package com.github.nazdov.slideshow.image.core.producer;

import com.github.nazdov.slideshow.image.core.dto.SlideshowEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;

public class SlideshowKafkaEventProducer implements ReactiveEventProducer<SlideshowEvent> {

    @Value("${slideshow.kafka.topic.name}")
    private String slideshowEventTopicName;
    private final ReactiveKafkaProducerTemplate<String, String> producerTemplate;

    public SlideshowKafkaEventProducer(ReactiveKafkaProducerTemplate<String, String> producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public Mono<Void> produce(SlideshowEvent slideshowEvent) {
        SenderRecord<String, String, String> record = SenderRecord
                .create(slideshowEvent.getTopic(),
                        slideshowEvent.getPartition(),
                        slideshowEvent.getTimestamp(),
                        slideshowEvent.getKey(),
                        slideshowEvent.getValue(), null);

        return producerTemplate.send(record).then();
    }

    public String getSlideshowEventTopicName() {
        return slideshowEventTopicName;
    }
}
