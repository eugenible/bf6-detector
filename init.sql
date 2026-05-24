CREATE SCHEMA IF NOT EXISTS bf6_detector_schema;
CREATE SCHEMA IF NOT EXISTS bf6_notifier_schema;

CREATE TABLE bf6_detector_schema.game_event_outbox
(
    id         UUID PRIMARY KEY,
    status     TEXT NULL,
    processed  BOOLEAN      DEFAULT FALSE,
    created_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS game_event_outbox_pending
    ON bf6_detector_schema.game_event_outbox (attempts, created_at)
    WHERE processed = FALSE;

CREATE TABLE bf6_notifier_schema.notification_logs
(
    id               SERIAL PRIMARY KEY,
    telegram_chat_id BIGINT NOT NULL,
    message          TEXT   NOT NULL,
    sent_at          TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
