-- Rides table
CREATE TABLE IF NOT EXISTS rides (
    id                    BIGSERIAL PRIMARY KEY,
    rider_id              BIGINT           NOT NULL REFERENCES riders (id),
    driver_id             BIGINT           REFERENCES drivers (id),
    vehicle_type          VARCHAR(32)      NOT NULL,
    status                VARCHAR(32)      NOT NULL DEFAULT 'REQUESTED',
    pickup_lat            DOUBLE PRECISION NOT NULL,
    pickup_lng            DOUBLE PRECISION NOT NULL,
    dropoff_lat           DOUBLE PRECISION NOT NULL,
    dropoff_lng           DOUBLE PRECISION NOT NULL,
    fare                  NUMERIC(12, 2),
    distance_km           DOUBLE PRECISION,
    estimated_duration_min INTEGER,
    started_at            TIMESTAMP,
    completed_at          TIMESTAMP,
    cancelled_at          TIMESTAMP,
    created_at            TIMESTAMP        NOT NULL DEFAULT now(),
    updated_at            TIMESTAMP        NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_rides_rider ON rides (rider_id);
CREATE INDEX IF NOT EXISTS idx_rides_driver ON rides (driver_id);
CREATE INDEX IF NOT EXISTS idx_rides_status ON rides (status);
