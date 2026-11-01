-- Drivers table
CREATE TABLE IF NOT EXISTS drivers (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120)  NOT NULL,
    email       VARCHAR(255)  NOT NULL UNIQUE,
    phone       VARCHAR(32)   NOT NULL UNIQUE,
    status      VARCHAR(32)  NOT NULL DEFAULT 'OFFLINE',
    rating      DOUBLE PRECISION NOT NULL DEFAULT 5.0,
    current_lat DOUBLE PRECISION,
    current_lng DOUBLE PRECISION,
    created_at  TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_drivers_status ON drivers (status);
CREATE INDEX IF NOT EXISTS idx_drivers_location ON drivers (current_lat, current_lng);
