package com.example.tpdevops.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RentalRecordTest {

    @Test
    void testConstructor() {
        LocalDateTime start = LocalDateTime.now();
        RentalRecord record = new RentalRecord("ABC123", "Alice", start);

        assertEquals("ABC123", record.getPlateNumber());
        assertEquals("Alice", record.getCustomerName());
        assertEquals(start, record.getStartDate());
        assertEquals("ACTIVE", record.getStatus());
        assertNull(record.getEndDate());
    }

    @Test
    void testSetEndDate() {
        RentalRecord record = new RentalRecord("ABC123", "Alice", LocalDateTime.now());
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        record.setEndDate(end);
        assertEquals(end, record.getEndDate());
    }

    @Test
    void testSetStatus() {
        RentalRecord record = new RentalRecord("ABC123", "Alice", LocalDateTime.now());
        record.setStatus("COMPLETED");
        assertEquals("COMPLETED", record.getStatus());
    }

    @Test
    void testSetTotalPrice() {
        RentalRecord record = new RentalRecord("ABC123", "Alice", LocalDateTime.now());
        record.setTotalPrice(150.0);
        assertEquals(150.0, record.getTotalPrice());
    }
}