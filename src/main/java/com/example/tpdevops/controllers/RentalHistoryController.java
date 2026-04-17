package com.example.tpdevops.controllers;

import com.example.tpdevops.entities.RentalRecord;
import com.example.tpdevops.services.RentalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalHistoryController {

    @Autowired
    private RentalHistoryService rentalHistoryService;

    @GetMapping
    public List<RentalRecord> getAllRecords() {
        return rentalHistoryService.getAllRecords();
    }

    @GetMapping("/active")
    public List<RentalRecord> getActiveRentals() {
        return rentalHistoryService.getActiveRentals();
    }

    @GetMapping("/customer/{customerName}")
    public List<RentalRecord> getRecordsByCustomer(@PathVariable String customerName) {
        return rentalHistoryService.getRecordsByCustomer(customerName);
    }

    @PostMapping("/start")
    public ResponseEntity<RentalRecord> startRental(
            @RequestParam String plateNumber,
            @RequestParam String customerName,
            @RequestParam double dailyPrice) {
        return ResponseEntity.ok(rentalHistoryService.startRental(plateNumber, customerName, dailyPrice));
    }

    @PutMapping("/end/{plateNumber}")
    public ResponseEntity<RentalRecord> endRental(@PathVariable String plateNumber) {
        return rentalHistoryService.endRental(plateNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}