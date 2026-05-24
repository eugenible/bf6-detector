package ru.eugenible.bf6.detector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eugenible.bf6.detector.entity.GameEventOutbox;
import ru.eugenible.bf6.detector.kafka.KafkaConfig;
import ru.eugenible.bf6.detector.repository.GameStartedOutboxRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusSendScheduler {

    private final GameStartedOutboxRepo outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void checkStatusEvents() {
        List<GameEventOutbox> pendingMessagesToProcess = outboxRepo.findPendingMessagesToProcess();

        if (pendingMessagesToProcess.isEmpty()) {
            return;
        }

        pendingMessagesToProcess.forEach(message -> {
            try {
                kafkaTemplate.send(
                        KafkaConfig.GAME_EVENTS_TOPIC,
                        message.getId().toString(),
                        message.getStatus().name()
                ).get();

                message.setProcessed(true);

                log.info("Sent to kafka: {}", message.getId());
            } catch (Exception e) {
                log.error("Kafka broker unavailable! Rolling back outbox batch for ID: {}", message.getId(), e);

                throw new RuntimeException("Kafka connection error", e);
            }
        });
    }
}
