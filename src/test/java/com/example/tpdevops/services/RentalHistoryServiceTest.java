package com.example.tpdevops.services;

import com.example.tpdevops.entities.RentalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RentalHistoryServiceTest {

    private RentalHistoryService service;

    @BeforeEach
    void setUp() {
        service = new RentalHistoryService();
    }

    @Test
    void testStartRental() {
        RentalRecord record = service.startRental("ABC123", "Alice", 50.0);
        assertEquals("ABC123", record.getPlateNumber());
        assertEquals("Alice", record.getCustomerName());
        assertEquals("ACTIVE", record.getStatus());
        assertEquals(50.0, record.getTotalPrice());
    }

    @Test
    void testEndRental() {
        service.startRental("ABC123", "Alice", 50.0);
        Optional<RentalRecord> result = service.endRental("ABC123");
        assertTrue(result.isPresent());
        assertEquals("COMPLETED", result.get().getStatus());
        assertNotNull(result.get().getEndDate());
        assertTrue(result.get().getTotalPrice() >= 50.0);
    }

    @Test
    void testEndRentalNotFound() {
        Optional<RentalRecord> result = service.endRental("UNKNOWN");
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllRecords() {
        service.startRental("ABC123", "Alice", 50.0);
        service.startRental("DEF456", "Bob", 80.0);
        assertEquals(2, service.getAllRecords().size());
    }

    @Test
    void testGetRecordsByCustomer() {
        service.startRental("ABC123", "Alice", 50.0);
        service.startRental("DEF456", "Bob", 80.0);
        service.startRental("GHI789", "Alice", 60.0);

        List<RentalRecord> aliceRecords = service.getRecordsByCustomer("Alice");
        assertEquals(2, aliceRecords.size());
    }

    @Test
    void testGetActiveRentals() {
        service.startRental("ABC123", "Alice", 50.0);
        service.startRental("DEF456", "Bob", 80.0);
        service.endRental("ABC123");

        List<RentalRecord> active = service.getActiveRentals();
        assertEquals(1, active.size());
        assertEquals("DEF456", active.get(0).getPlateNumber());
    }

    @Test
    void testClearAll() {
        service.startRental("ABC123", "Alice", 50.0);
        service.clearAll();
        assertTrue(service.getAllRecords().isEmpty());
    }
}