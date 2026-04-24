package com.example.tpdevops.services;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.entities.RentalRecord;
import com.example.tpdevops.entities.RentalStatus;
import com.example.tpdevops.exception.ActiveRentalConflictException;
import com.example.tpdevops.exception.CarNotFoundException;
import com.example.tpdevops.repositories.CarRepository;
import com.example.tpdevops.repositories.RentalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Cohérence de l’historique de location : toute fiche en statut actif a une seule
 * clé (véhicule) grâce au modèle (cf. indice d’unicité en migration) et des contrôles applicatifs.
 */
@Service
@Transactional
public class RentalHistoryService {

    private final CarRepository carRepository;
    private final RentalRecordRepository rentalRecordRepository;

    public RentalHistoryService(CarRepository carRepository, RentalRecordRepository rentalRecordRepository) {
        this.carRepository = carRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<RentalRecord> getAllRecords() {
        return rentalRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RentalRecord> getActiveRentals() {
        return rentalRecordRepository.findByStatusOrderByStartDateDesc(RentalStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<RentalRecord> getRecordsByCustomer(String customerName) {
        return rentalRecordRepository.findByCustomerName(customerName);
    }

    public RentalRecord startRental(String plateNumber, String customerName, double dailyPrice) {
        Car car = carRepository
            .findById(plateNumber)
            .orElseThrow(() -> new CarNotFoundException("Voiture introuvable : " + plateNumber));

        if (rentalRecordRepository.findByCar_PlateNumberAndStatus(plateNumber, RentalStatus.ACTIVE).isPresent()) {
            throw new ActiveRentalConflictException("Location déjà active pour la voiture : " + plateNumber);
        }

        RentalRecord rental = new RentalRecord();
        rental.setCar(car);
        rental.setCustomerName(customerName);
        rental.setStartDate(LocalDateTime.now());
        rental.setTotalPrice(dailyPrice);
        rental.setStatus(RentalStatus.ACTIVE);
        return rentalRecordRepository.save(rental);
    }

    public Optional<RentalRecord> endRental(String plateNumber) {
        return rentalRecordRepository
            .findByCar_PlateNumberAndStatus(plateNumber, RentalStatus.ACTIVE)
            .map(r -> {
                r.setEndDate(LocalDateTime.now());
                r.setStatus(RentalStatus.COMPLETED);
                long days = Math.max(1, ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()));
                r.setTotalPrice(r.getTotalPrice() * days);
                return rentalRecordRepository.save(r);
            });
    }
}
