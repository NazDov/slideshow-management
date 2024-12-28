package com.github.nazdov.slideshow.image.core.config;

import com.github.nazdov.slideshow.image.core.producer.SlideshowKafkaEventProducer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(
        value = {KafkaProperties.class}
)
public class SlideshowKafkaProducerConfig {

    @Value("${slideshow.kafka.topic.name}")
    private String topicName;

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> producerTemplate(KafkaProperties kafkaProperties) {
        // Create a map for Kafka producer configurations
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("bootstrap.servers", kafkaProperties
                .getBootstrapServers().stream().findAny().orElseThrow());  // Kafka broker address
        producerProps.put("key.serializer", StringSerializer.class);
        producerProps.put("value.serializer", StringSerializer.class);

        // Set up sender options with the producer properties
        SenderOptions<String, String> senderOptions = SenderOptions.create(producerProps);

        // Return ReactiveKafkaProducerTemplate
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }

    @Bean
    public NewTopic taskTopic() {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public SlideshowKafkaEventProducer slideshowKafkaEventProducer(ReactiveKafkaProducerTemplate<String, String> producerTemplate) {
        return new SlideshowKafkaEventProducer(producerTemplate);
    }
}
