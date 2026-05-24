package ru.eugenible.bf6.detector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.eugenible.bf6.detector.entity.GameEventOutbox;

import java.util.List;
import java.util.UUID;

public interface GameStartedOutboxRepo extends JpaRepository<GameEventOutbox, UUID> {

    @Query("""
            SELECT o
            FROM GameEventOutbox o
            WHERE o.processed = false AND o.attempts < 10
            ORDER BY o.createdAt ASC
            """)
    List<GameEventOutbox> findPendingMessagesToProcess();

}
