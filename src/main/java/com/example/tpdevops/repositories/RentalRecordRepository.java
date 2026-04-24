package com.example.tpdevops.repositories;

import com.example.tpdevops.entities.RentalRecord;
import com.example.tpdevops.entities.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Persistance des fiches de location, avec critères d’intérêt pour la couche service
 * (statut, client) sans exposer SQL de premier niveau.
 */
@Repository
public interface RentalRecordRepository extends JpaRepository<RentalRecord, Long> {

    /**
     * Prédicat sur le véhicule associé (naviguation {@code car → plateNumber}) + statut.
     * L’« underscore » dans le nom de méthode sert ici d’accolade explicite pour la clé
     * étrangère composite logique, tel que décrit dans Spring Data JPA.
     */
    Optional<RentalRecord> findByCar_PlateNumberAndStatus(String plateNumber, RentalStatus status);

    List<RentalRecord> findByCustomerName(String customerName);

    List<RentalRecord> findByStatusOrderByStartDateDesc(RentalStatus status);
}
