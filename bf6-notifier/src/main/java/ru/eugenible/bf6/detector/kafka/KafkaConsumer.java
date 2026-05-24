package ru.eugenible.bf6.detector.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import ru.eugenible.bf6.detector.service.GameEventNotifier;

@EnableKafka
@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final GameEventNotifier gameEventNotifier;

    @KafkaListener(
            topics = KafkaConfig.GAME_EVENTS_TOPIC,
            groupId = "bf6-notifier"
    )
    public void consumeBfStatus(String message) {
        log.info("Получено сообщение о том, что с бф что-то произошло! {}", message);

        String msg;

        if (message.equals("STARTED")) {
            msg = "Еееебать! А батла-то запущена! Кто-то щас жестко катнет в поле боя! Нихуево!";
        } else {
            msg = "Кентишка наигрался... Пора и про реальный мир вспомнить.";
        }

        gameEventNotifier.sendMessageToChannel(msg);
    }

}
