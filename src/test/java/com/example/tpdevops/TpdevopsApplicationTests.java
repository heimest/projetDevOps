package com.example.tpdevops;

import com.example.tpdevops.testsupport.AbstractPostgresIT;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TpdevopsApplicationTests extends AbstractPostgresIT {

    /**
     * Fumeur d’intégration : vérification explicite que le contexte Spring, la datasource
     * (Testcontainers, profil {@code test}), Flyway et les {@code Bean} repository sont
     * disponibles. Sans assertion, l’exécution se terminerait aussi en vert si l’injection
     * était cassée, ce que les règles JUnit et Sonar rejettent.
     */
    @Test
    void contextLoads() {
        assertThat(carRepository).isNotNull();
        assertThat(rentalRecordRepository).isNotNull();
    }
}
