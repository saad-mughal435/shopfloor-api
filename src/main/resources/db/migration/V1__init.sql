-- ShopFloor API — initial schema (PostgreSQL).
-- Column names are snake_case to match Spring Boot's default physical naming
-- strategy, so Hibernate's `ddl-auto: validate` passes against this schema.

CREATE TABLE app_user (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(64)  NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL
);

CREATE TABLE production_line (
    id                   BIGSERIAL PRIMARY KEY,
    code                 VARCHAR(32)  NOT NULL UNIQUE,
    name                 VARCHAR(128) NOT NULL,
    rated_units_per_hour INTEGER      NOT NULL
);

CREATE TABLE job_order (
    id                      BIGSERIAL PRIMARY KEY,
    order_no                VARCHAR(40)  NOT NULL UNIQUE,
    line_id                 BIGINT       NOT NULL REFERENCES production_line (id),
    product                 VARCHAR(128) NOT NULL,
    planned_qty             INTEGER      NOT NULL,
    planned_runtime_minutes INTEGER      NOT NULL,
    status                  VARCHAR(20)  NOT NULL,
    good_units              INTEGER,
    reject_units            INTEGER,
    downtime_minutes        INTEGER,
    availability            NUMERIC(5, 4),
    performance             NUMERIC(5, 4),
    quality                 NUMERIC(5, 4),
    oee                     NUMERIC(5, 4),
    created_at              TIMESTAMP    NOT NULL,
    started_at              TIMESTAMP,
    closed_at               TIMESTAMP
);

CREATE TABLE downtime_event (
    id           BIGSERIAL PRIMARY KEY,
    job_order_id BIGINT       NOT NULL REFERENCES job_order (id),
    minutes      INTEGER      NOT NULL,
    reason       VARCHAR(160) NOT NULL,
    root_cause   VARCHAR(255),
    occurred_at  TIMESTAMP    NOT NULL
);

CREATE TABLE qc_hold (
    id           BIGSERIAL PRIMARY KEY,
    job_order_id BIGINT REFERENCES job_order (id),
    reason       VARCHAR(255) NOT NULL,
    severity     VARCHAR(20)  NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    raised_by    VARCHAR(64)  NOT NULL,
    raised_at    TIMESTAMP    NOT NULL,
    released_at  TIMESTAMP
);

CREATE TABLE inventory_item (
    id   BIGSERIAL PRIMARY KEY,
    sku  VARCHAR(40)  NOT NULL UNIQUE,
    name VARCHAR(160) NOT NULL,
    uom  VARCHAR(16)  NOT NULL
);

CREATE TABLE stock_lot (
    id                 BIGSERIAL PRIMARY KEY,
    item_id            BIGINT         NOT NULL REFERENCES inventory_item (id),
    quantity_remaining NUMERIC(14, 3) NOT NULL,
    unit_cost          NUMERIC(14, 4) NOT NULL,
    received_at        TIMESTAMP      NOT NULL
);

CREATE TABLE stock_movement (
    id         BIGSERIAL PRIMARY KEY,
    item_id    BIGINT         NOT NULL REFERENCES inventory_item (id),
    type       VARCHAR(20)    NOT NULL,
    quantity   NUMERIC(14, 3) NOT NULL,
    reference  VARCHAR(120),
    created_at TIMESTAMP      NOT NULL
);

CREATE INDEX idx_job_order_line ON job_order (line_id);
CREATE INDEX idx_downtime_job_order ON downtime_event (job_order_id);
CREATE INDEX idx_stock_lot_item_received ON stock_lot (item_id, received_at);
CREATE INDEX idx_stock_movement_item ON stock_movement (item_id);
