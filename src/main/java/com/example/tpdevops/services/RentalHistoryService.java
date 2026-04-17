package com.example.tpdevops.services;

import com.example.tpdevops.entities.RentalRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RentalHistoryService {

    private final List<RentalRecord> records = new ArrayList<>();

    public RentalRecord startRental(String plateNumber, String customerName, double dailyPrice) {
        RentalRecord rental = new RentalRecord(plateNumber, customerName, LocalDateTime.now());
        rental.setTotalPrice(dailyPrice);
        records.add(rental);
        return rental;
    }

    public Optional<RentalRecord> endRental(String plateNumber) {
        return records.stream()
                .filter(r -> r.getPlateNumber().equals(plateNumber) && "ACTIVE".equals(r.getStatus()))
                .findFirst()
                .map(r -> {
                    r.setEndDate(LocalDateTime.now());
                    r.setStatus("COMPLETED");
                    long days = Math.max(1, ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()));
                    r.setTotalPrice(r.getTotalPrice() * days);
                    return r;
                });
    }

    public List<RentalRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public List<RentalRecord> getRecordsByCustomer(String customerName) {
        return records.stream()
                .filter(r -> r.getCustomerName().equals(customerName))
                .toList();
    }

    public List<RentalRecord> getActiveRentals() {
        return records.stream()
                .filter(r -> "ACTIVE".equals(r.getStatus()))
                .toList();
    }

    public void clearAll() {
        records.clear();
    }
}
