-- Ride events (lifecycle + location traces)
CREATE TABLE IF NOT EXISTS ride_events (
    id         BIGSERIAL PRIMARY KEY,
    ride_id    BIGINT      NOT NULL REFERENCES rides (id),
    event_type VARCHAR(64) NOT NULL,
    status     VARCHAR(32) NOT NULL,
    latitude   DOUBLE PRECISION,
    longitude  DOUBLE PRECISION,
    payload    TEXT,
    created_at TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_ride_events_ride ON ride_events (ride_id, created_at);
