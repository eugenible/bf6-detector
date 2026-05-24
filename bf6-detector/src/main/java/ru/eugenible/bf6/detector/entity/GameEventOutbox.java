package ru.eugenible.bf6.detector.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.eugenible.bf6.detector.enums.GameStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Access(AccessType.FIELD)
@Setter
@Getter
@Entity
@Table(schema = "bf6_detector_schema", name = "game_event_outbox")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class GameEventOutbox {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private GameStatus status;

    @Column(name = "processed")
    private boolean processed;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

}
