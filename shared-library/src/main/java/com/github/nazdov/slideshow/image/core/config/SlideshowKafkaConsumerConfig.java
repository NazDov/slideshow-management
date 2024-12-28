package com.github.nazdov.slideshow.image.core.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(
        value = {
                KafkaProperties.class
        }
)
public class SlideshowKafkaConsumerConfig {

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> consumerTemplate(KafkaProperties kafkaProperties) {
        // Create a map for Kafka consumer configurations
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put("bootstrap.servers", kafkaProperties
                .getBootstrapServers().stream().findAny().orElseThrow());  // Kafka broker address
        consumerProps.put("group.id", "consumer-group");
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", StringDeserializer.class);

        // Set up receiver options with the consumer properties
        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.create(consumerProps);

        // Return ReactiveKafkaConsumerTemplate
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }
}
