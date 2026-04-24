-- cible: PostgreSQL (dialecte 16) ; exécuté par Flyway, pas en direct depuis l'IDE
/*
 * Schéma initial (PostgreSQL).
 * Flyway = gestion de versions du schéma, comparable à Alembic en écosystème Java/Spring.
 * Les scripts sont exécutés exactement une fois, dans l’ordre, sur chaque environnement.
 */
CREATE TABLE cars (
    plate_number VARCHAR(64) PRIMARY KEY,
    brand        VARCHAR(255) NOT NULL,
    price        DOUBLE PRECISION NOT NULL,
    available    BOOLEAN         NOT NULL DEFAULT true
);

CREATE TABLE rental_records (
    id            BIGSERIAL PRIMARY KEY,
    car_plate     VARCHAR(64) NOT NULL
        REFERENCES cars (plate_number) ON DELETE CASCADE,
    customer_name VARCHAR(255) NOT NULL,
    start_date    TIMESTAMP NOT NULL,
    end_date      TIMESTAMP,
    total_price   DOUBLE PRECISION NOT NULL,
    status        VARCHAR(32)  NOT NULL
        CHECK (status IN ('ACTIVE', 'COMPLETED'))
);

CREATE INDEX ix_rental_records_customer_name ON rental_records (customer_name);
CREATE INDEX ix_rental_records_status        ON rental_records (status);

/*
 * Règle métier : une seule location active par véhicule à un instant t.
 * Contrainte « partielle » supportée par PostgreSQL (mieux qu’un check générique).
 */
CREATE UNIQUE INDEX uq_rental_one_active_per_plate
    ON rental_records (car_plate)
    WHERE status = 'ACTIVE';
