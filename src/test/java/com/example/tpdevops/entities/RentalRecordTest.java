package com.example.tpdevops.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RentalRecordTest {

    @Test
    void testConstructor() {
        LocalDateTime start = LocalDateTime.now();
        Car car = new Car("ABC123", "T", 1.0);
        RentalRecord rental = new RentalRecord(car, "Alice", start);

        assertEquals("ABC123", rental.getPlateNumber());
        assertEquals("Alice", rental.getCustomerName());
        assertEquals(start, rental.getStartDate());
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
        assertNull(rental.getEndDate());
    }

    @Test
    void testSetEndDate() {
        Car car = new Car("ABC123", "T", 1.0);
        RentalRecord rental = new RentalRecord(car, "Alice", LocalDateTime.now());
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        rental.setEndDate(end);
        assertEquals(end, rental.getEndDate());
    }

    @Test
    void testSetStatus() {
        Car car = new Car("ABC123", "T", 1.0);
        RentalRecord rental = new RentalRecord(car, "Alice", LocalDateTime.now());
        rental.setStatus(RentalStatus.COMPLETED);
        assertEquals(RentalStatus.COMPLETED, rental.getStatus());
    }

    @Test
    void testSetTotalPrice() {
        Car car = new Car("ABC123", "T", 1.0);
        RentalRecord rental = new RentalRecord(car, "Alice", LocalDateTime.now());
        rental.setTotalPrice(150.0);
        assertEquals(150.0, rental.getTotalPrice());
    }
}
