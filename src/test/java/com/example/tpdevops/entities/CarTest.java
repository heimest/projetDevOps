package com.example.tpdevops.entities;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void testCarConstructor() {
        Car car = new Car("ABC123", "Toyota", 15000.0);

        assertEquals("ABC123", car.getPlateNumber());
        assertEquals("Toyota", car.getBrand());
        assertEquals(15000.0, car.getPrice());
        assertTrue(car.isAvailable()); // disponible par défaut
    }

    @Test
    void testDefaultConstructorAvailable() {
        Car car = new Car();
        assertTrue(car.isAvailable());
    }

    @Test
    void testSetAvailableFalse() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setAvailable(false);
        assertFalse(car.isAvailable());
    }

    @Test
    void testSetAvailableTrue() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setAvailable(false);
        car.setAvailable(true);
        assertTrue(car.isAvailable());
    }

    @Test
    void testSetters() {
        Car car = new Car();
        car.setPlateNumber("XYZ789");
        car.setBrand("BMW");
        car.setPrice(25000.0);

        assertEquals("XYZ789", car.getPlateNumber());
        assertEquals("BMW", car.getBrand());
        assertEquals(25000.0, car.getPrice());
    }

    @Nested
    class EqualsAndHashCode {
        @Test
        void equalsIsTrueForSameReference() {
            Car a = new Car("P1", "B", 1.0);
            assertEquals(a, a);
        }

        @Test
        void equalsIsFalseForNull() {
            // L’entité en premier : Objects.equals(e, o) en délègue à e.equals(o) (même exécution qu’avant).
            assertNotEquals(new Car("P1", "B", 1.0), null);
        }

        @Test
        void equalsIsFalseForOtherClass() {
            assertNotEquals(new Car("P1", "B", 1.0), "not a car");
        }

        @Test
        void equalsByPlateWhenBothPlatesSet() {
            Car a = new Car("P1", "A", 1.0);
            Car b = new Car("P1", "B", 2.0);
            assertEquals(a, b);
        }

        @Test
        void notEqualsWhenPlatesDiffer() {
            Car a = new Car("P1", "B", 1.0);
            Car b = new Car("P2", "B", 1.0);
            assertNotEquals(a, b);
        }

        @Test
        void notEqualsWhenThisPlateIsNull() {
            Car a = new Car();
            a.setPlateNumber(null);
            Car b = new Car("P1", "B", 1.0);
            assertNotEquals(a, b);
        }

        @Test
        void notEqualsWhenOtherPlateIsNull() {
            Car a = new Car("P1", "B", 1.0);
            Car b = new Car();
            b.setPlateNumber(null);
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeMatchesWhenEqual() {
            Car a = new Car("P1", "A", 1.0);
            Car b = new Car("P1", "B", 2.0);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCodeForNullPlate() {
            Car a = new Car();
            a.setPlateNumber(null);
            assertEquals(0, a.hashCode());
        }
    }
}
