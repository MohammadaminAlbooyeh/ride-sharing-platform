-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id           BIGSERIAL PRIMARY KEY,
    driver_id    BIGINT       NOT NULL UNIQUE REFERENCES drivers (id),
    type         VARCHAR(32)  NOT NULL,
    model        VARCHAR(120) NOT NULL,
    plate_number VARCHAR(20)  NOT NULL UNIQUE,
    capacity     INTEGER      NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT now()
);
