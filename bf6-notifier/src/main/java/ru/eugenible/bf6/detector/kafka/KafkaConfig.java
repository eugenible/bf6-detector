package ru.eugenible.bf6.detector.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaConfig {

    public static final String GAME_EVENTS_TOPIC = "bf6.game.events";

    @Bean
    public NewTopic gameEventsTopic() {
        return TopicBuilder.name(GAME_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
