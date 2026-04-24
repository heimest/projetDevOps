package com.example.tpdevops.controllers;

import com.example.tpdevops.dto.RentalRecordDtoMapper;
import com.example.tpdevops.dto.RentalRecordResponseDto;
import com.example.tpdevops.services.RentalHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalHistoryController {

    private final RentalHistoryService rentalHistoryService;

    public RentalHistoryController(RentalHistoryService rentalHistoryService) {
        this.rentalHistoryService = rentalHistoryService;
    }

    @GetMapping
    public List<RentalRecordResponseDto> getAllRecords() {
        return rentalHistoryService.getAllRecords()
            .stream()
            .map(RentalRecordDtoMapper::toResponse)
            .toList();
    }

    @GetMapping("/active")
    public List<RentalRecordResponseDto> getActiveRentals() {
        return rentalHistoryService.getActiveRentals()
            .stream()
            .map(RentalRecordDtoMapper::toResponse)
            .toList();
    }

    @GetMapping("/customer/{customerName}")
    public List<RentalRecordResponseDto> getRecordsByCustomer(@PathVariable String customerName) {
        return rentalHistoryService.getRecordsByCustomer(customerName)
            .stream()
            .map(RentalRecordDtoMapper::toResponse)
            .toList();
    }

    @PostMapping("/start")
    public ResponseEntity<RentalRecordResponseDto> startRental(
            @RequestParam String plateNumber,
            @RequestParam String customerName,
            @RequestParam double dailyPrice) {
        return ResponseEntity.ok(RentalRecordDtoMapper.toResponse(
            rentalHistoryService.startRental(plateNumber, customerName, dailyPrice)
        ));
    }

    @PutMapping("/end/{plateNumber}")
    public ResponseEntity<RentalRecordResponseDto> endRental(@PathVariable String plateNumber) {
        return rentalHistoryService.endRental(plateNumber)
            .map(RentalRecordDtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
