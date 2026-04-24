package com.example.tpdevops.testsupport;

import com.example.tpdevops.repositories.CarRepository;
import com.example.tpdevops.repositories.RentalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Fondation d’intégration : l’URL {@code jdbc:tc:postgresql:} (voir
 * {@code application-test.properties}) pousse un PostgreSQL réel dans le même
 * moteur que l’infrastructure, sans conflit entre contextes d’application Spring
 * mis en cache par type de test.
 */
@Tag("integration")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractPostgresIT {

    @Autowired
    protected CarRepository carRepository;

    @Autowired
    protected RentalRecordRepository rentalRecordRepository;

    @BeforeEach
    void truncateCoreTables() {
        rentalRecordRepository.deleteAllInBatch();
        carRepository.deleteAllInBatch();
    }
}
