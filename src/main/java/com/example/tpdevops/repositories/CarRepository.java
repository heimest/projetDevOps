package com.example.tpdevops.repositories;

import com.example.tpdevops.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Accès porté (DDD « repository ») au parc de véhicules.
 * Spring Data fournit l’implémentation à l’exécution et ouvre l’extensibilité des requêtes déclaratives.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, String> {
}
