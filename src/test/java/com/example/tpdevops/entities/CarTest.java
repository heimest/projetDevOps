package com.example.tpdevops.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarTest {

    @Test
    public void testCarConstructor() {
        Car car = new Car("ABC123", "Toyota", 15000.0);

        assertEquals("ABC123", car.getPlateNumber());
        assertEquals("Toyota", car.getBrand());
        assertEquals(15000.0, car.getPrice());
        assertTrue(car.isAvailable()); // disponible par défaut
    }

    @Test
    public void testDefaultConstructorAvailable() {
        Car car = new Car();
        assertTrue(car.isAvailable());
    }

    @Test
    public void testSetAvailableFalse() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setAvailable(false);
        assertFalse(car.isAvailable());
    }

    @Test
    public void testSetAvailableTrue() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setAvailable(false);
        car.setAvailable(true);
        assertTrue(car.isAvailable());
    }

    @Test
    public void testSetters() {
        Car car = new Car();
        car.setPlateNumber("XYZ789");
        car.setBrand("BMW");
        car.setPrice(25000.0);

        assertEquals("XYZ789", car.getPlateNumber());
        assertEquals("BMW", car.getBrand());
        assertEquals(25000.0, car.getPrice());
    }
}