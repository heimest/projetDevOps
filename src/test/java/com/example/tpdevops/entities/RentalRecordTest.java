package com.example.tpdevops.entities;

import org.junit.jupiter.api.Nested;
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

    @Test
    void noArgConstructorAndSettersCoversJpaPath() {
        RentalRecord r = new RentalRecord();
        Car car = new Car("Z1", "Mazda", 2.0);
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 12, 0);
        r.setCar(car);
        r.setCustomerName("Zed");
        r.setStartDate(start);
        r.setEndDate(null);
        r.setTotalPrice(9.0);
        r.setStatus(RentalStatus.ACTIVE);
        r.setId(99L);

        assertEquals(99L, r.getId());
        assertEquals(car, r.getCar());
        assertEquals("Z1", r.getPlateNumber());
        assertEquals("Zed", r.getCustomerName());
        assertEquals(start, r.getStartDate());
        assertNull(r.getEndDate());
        assertEquals(9.0, r.getTotalPrice());
        assertEquals(RentalStatus.ACTIVE, r.getStatus());
    }

    @Test
    void getPlateNumberReturnsNullWhenCarIsNull() {
        RentalRecord r = new RentalRecord();
        assertNull(r.getPlateNumber());
    }

    @Test
    void getCarAndSetCar() {
        RentalRecord r = new RentalRecord();
        Car c = new Car("C1", "D", 1.0);
        r.setCar(c);
        assertEquals(c, r.getCar());
        r.setCar(null);
        assertNull(r.getCar());
    }

    @Nested
    class EqualsAndHashCode {
        @Test
        void equalsIsTrueForSameReference() {
            RentalRecord r = new RentalRecord();
            r.setId(1L);
            assertEquals(r, r);
        }

        @Test
        void equalsIsFalseForNull() {
            RentalRecord r = new RentalRecord();
            r.setId(1L);
            assertNotEquals(r, null);
        }

        @Test
        void equalsIsFalseForOtherClass() {
            RentalRecord r = new RentalRecord();
            r.setId(1L);
            assertNotEquals(r, "x");
        }

        @Test
        void equalsByIdWhenBothNonNullAndEqual() {
            RentalRecord a = new RentalRecord();
            a.setId(42L);
            RentalRecord b = new RentalRecord();
            b.setId(42L);
            assertEquals(a, b);
        }

        @Test
        void notEqualsWhenBothIdsNonNullButDifferent() {
            RentalRecord a = new RentalRecord();
            a.setId(1L);
            RentalRecord b = new RentalRecord();
            b.setId(2L);
            assertNotEquals(a, b);
        }

        @Test
        void notEqualsWhenThisIdIsNull() {
            RentalRecord a = new RentalRecord();
            RentalRecord b = new RentalRecord();
            b.setId(1L);
            assertNotEquals(a, b);
        }

        @Test
        void notEqualsWhenOtherIdIsNull() {
            RentalRecord a = new RentalRecord();
            a.setId(1L);
            RentalRecord b = new RentalRecord();
            assertNotEquals(a, b);
        }

        @Test
        void notEqualsWhenBothIdsNull() {
            RentalRecord a = new RentalRecord();
            RentalRecord b = new RentalRecord();
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeUsesId() {
            RentalRecord a = new RentalRecord();
            a.setId(7L);
            assertEquals(Long.hashCode(7L), a.hashCode());
        }

        @Test
        void hashCodeWhenIdNull() {
            assertEquals(0, new RentalRecord().hashCode());
        }
    }
}
