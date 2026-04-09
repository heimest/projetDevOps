package com.example.tpdevops.services;

import com.example.tpdevops.entities.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {

    private CarService carService;

    @BeforeEach
    public void setUp() {
        carService = new CarService();
    }

    @Test
    public void testAddCar() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        Car added = carService.addCar(car);

        assertEquals("ABC123", added.getPlateNumber());
        assertEquals(1, carService.getCars().size());
    }

    @Test
    public void testAddDuplicateCarThrowsException() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        assertThrows(IllegalArgumentException.class,
                () -> carService.addCar(new Car("ABC123", "Honda", 12000.0)));
    }

    @Test
    public void testGetCarsReturnsAll() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "BMW", 25000.0));

        List<Car> cars = carService.getCars();
        assertEquals(2, cars.size());
    }

    @Test
    public void testGetCarByPlateNumberFound() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        Optional<Car> car = carService.getCarByPlateNumber("ABC123");
        assertTrue(car.isPresent());
        assertEquals("Toyota", car.get().getBrand());
    }

    @Test
    public void testGetCarByPlateNumberNotFound() {
        Optional<Car> car = carService.getCarByPlateNumber("UNKNOWN");
        assertFalse(car.isPresent());
    }

    @Test
    public void testDeleteCarSuccess() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        assertTrue(carService.deleteCar("ABC123"));
        assertEquals(0, carService.getCars().size());
    }

    @Test
    public void testDeleteNonExistentCarReturnsFalse() {
        assertFalse(carService.deleteCar("UNKNOWN"));
    }

    @Test
    public void testRentCarSetsUnavailable() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        Car rented = carService.rentCar("ABC123");
        assertFalse(rented.isAvailable());
    }

    @Test
    public void testRentAlreadyRentedCarThrowsException() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.rentCar("ABC123");

        assertThrows(IllegalStateException.class, () -> carService.rentCar("ABC123"));
    }

    @Test
    public void testRentNonExistentCarThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> carService.rentCar("UNKNOWN"));
    }

    @Test
    public void testReturnCarSetsAvailable() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.rentCar("ABC123");

        Car returned = carService.returnCar("ABC123");
        assertTrue(returned.isAvailable());
    }

    @Test
    public void testReturnAvailableCarThrowsException() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        assertThrows(IllegalStateException.class, () -> carService.returnCar("ABC123"));
    }

    @Test
    public void testReturnNonExistentCarThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> carService.returnCar("UNKNOWN"));
    }
}