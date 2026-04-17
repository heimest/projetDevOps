package com.example.tpdevops.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RentalRecordTest {

    @Test
    void testConstructor() {
        LocalDateTime start = LocalDateTime.now();
        RentalRecord rental = new RentalRecord("ABC123", "Alice", start);

        assertEquals("ABC123", rental.getPlateNumber());
        assertEquals("Alice", rental.getCustomerName());
        assertEquals(start, rental.getStartDate());
        assertEquals("ACTIVE", rental.getStatus());
        assertNull(rental.getEndDate());
    }

    @Test
    void testSetEndDate() {
        RentalRecord rental = new RentalRecord("ABC123", "Alice", LocalDateTime.now());
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        rental.setEndDate(end);
        assertEquals(end, rental.getEndDate());
    }

    @Test
    void testSetStatus() {
        RentalRecord rental = new RentalRecord("ABC123", "Alice", LocalDateTime.now());
        rental.setStatus("COMPLETED");
        assertEquals("COMPLETED", rental.getStatus());
    }

    @Test
    void testSetTotalPrice() {
        RentalRecord rental = new RentalRecord("ABC123", "Alice", LocalDateTime.now());
        rental.setTotalPrice(150.0);
        assertEquals(150.0, rental.getTotalPrice());
    }
}
