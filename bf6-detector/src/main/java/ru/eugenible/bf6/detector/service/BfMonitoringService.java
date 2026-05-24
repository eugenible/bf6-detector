package ru.eugenible.bf6.detector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eugenible.bf6.detector.entity.GameEventOutbox;
import ru.eugenible.bf6.detector.enums.GameStatus;
import ru.eugenible.bf6.detector.repository.GameStartedOutboxRepo;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BfMonitoringService {

    public static final String BF6_PROCESS_NAME = "bf6.exe";

    private final GameStartedOutboxRepo gameStartedOutboxRepo;

    private boolean isGameCurrentlyRunning = false;

    /**
     * Polls the Windows process list every 2 seconds.
     * Adjust fixedDelay string based on how aggressive you want the tracking to be.
     */
    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void checkBf6Process() {
        boolean isRunningNow = isProcessRunning(BF6_PROCESS_NAME);

        if (isRunningNow && !isGameCurrentlyRunning) {
            log.info("🎯 Detection: {} has just been started!", BF6_PROCESS_NAME);

            saveGameStartedEvent(GameStatus.STARTED);
            isGameCurrentlyRunning = true;
        } else if (!isRunningNow && isGameCurrentlyRunning) {
            log.info("💤 Detection: {} has been closed.", BF6_PROCESS_NAME);

            isGameCurrentlyRunning = false;
            saveGameStartedEvent(GameStatus.EXITED);
        }
    }

    /**
     * Scans active OS processes using Java's native ProcessHandle API
     */
    private boolean isProcessRunning(String processName) {
        return ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(info -> info.command().orElse(""))
                .anyMatch(commandPath -> commandPath.endsWith(processName));
    }

    /**
     * Creates the event payload string and writes it directly to your outbox table
     */
    private void saveGameStartedEvent(GameStatus status) {
        GameEventOutbox event = new GameEventOutbox();

        event.setStatus(status);
        event.setProcessed(false);
        event.setAttempts(0);
        event.setCreatedAt(OffsetDateTime.now());

        gameStartedOutboxRepo.save(event);

        log.info("💾 Outbox event written to database for async Kafka processing.");
    }

}
