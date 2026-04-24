package com.example.tpdevops.services;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.entities.RentalRecord;
import com.example.tpdevops.entities.RentalStatus;
import com.example.tpdevops.exception.ActiveRentalConflictException;
import com.example.tpdevops.exception.CarNotFoundException;
import com.example.tpdevops.testsupport.AbstractPostgresIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RentalHistoryServiceIT extends AbstractPostgresIT {

    @Autowired
    private RentalHistoryService service;

    @Test
    void startRental() {
        carRepository.save(new Car("ABC123", "Toyota", 1500.0));
        RentalRecord rental = service.startRental("ABC123", "Alice", 50.0);
        assertEquals("ABC123", rental.getPlateNumber());
        assertEquals("Alice", rental.getCustomerName());
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
        assertEquals(50.0, rental.getTotalPrice());
    }

    @Test
    void startRentalFailsWhenCarMissing() {
        assertThrows(
            CarNotFoundException.class,
            () -> service.startRental("NOCAR", "Eve", 1.0)
        );
    }

    @Test
    void startRentalOnlyOneActive() {
        carRepository.save(new Car("DUP01", "Peugeot", 900.0));
        service.startRental("DUP01", "A", 10.0);
        assertThrows(ActiveRentalConflictException.class, () -> service.startRental("DUP01", "B", 20.0));
    }

    @Test
    void endRental() {
        carRepository.save(new Car("ABC123", "Toyota", 1500.0));
        service.startRental("ABC123", "Alice", 50.0);
        Optional<RentalRecord> result = service.endRental("ABC123");
        assertTrue(result.isPresent());
        assertEquals(RentalStatus.COMPLETED, result.get().getStatus());
        assertNotNull(result.get().getEndDate());
        assertTrue(result.get().getTotalPrice() >= 50.0);
    }

    @Test
    void endRentalNotFound() {
        assertTrue(service.endRental("NOPLATE").isEmpty());
    }

    @Test
    void endRentalWhenAlreadyClosedReturnsEmpty() {
        carRepository.save(new Car("K1", "B", 1.0));
        service.startRental("K1", "A", 1.0);
        assertTrue(service.endRental("K1").isPresent());
        assertTrue(service.endRental("K1").isEmpty());
    }

    @Test
    void getAllRecords() {
        carRepository.save(new Car("A1", "T", 1.0));
        carRepository.save(new Car("A2", "B", 1.0));
        service.startRental("A1", "Alice", 50.0);
        service.startRental("A2", "Bob", 80.0);
        assertEquals(2, service.getAllRecords().size());
    }

    @Test
    void getRecordsByCustomer() {
        carRepository.save(new Car("A1", "T", 1.0));
        carRepository.save(new Car("A2", "B", 1.0));
        carRepository.save(new Car("A3", "B", 1.0));
        service.startRental("A1", "Alice", 50.0);
        service.startRental("A2", "Bob", 80.0);
        service.startRental("A3", "Alice", 60.0);

        List<RentalRecord> aliceRecords = service.getRecordsByCustomer("Alice");
        assertEquals(2, aliceRecords.size());
    }

    @Test
    void getActiveRentals() {
        carRepository.save(new Car("A1", "T", 1.0));
        carRepository.save(new Car("A2", "B", 1.0));
        service.startRental("A1", "Alice", 50.0);
        service.startRental("A2", "Bob", 80.0);
        service.endRental("A1");

        List<RentalRecord> active = service.getActiveRentals();
        assertEquals(1, active.size());
        assertEquals("A2", active.get(0).getPlateNumber());
    }
}
