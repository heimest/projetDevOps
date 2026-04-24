package com.example.tpdevops.services;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.testsupport.AbstractPostgresIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceIT extends AbstractPostgresIT {

    @Autowired
    private CarService carService;

    @Test
    void addCar() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        Car added = carService.addCar(car);

        assertEquals("ABC123", added.getPlateNumber());
        assertEquals(1, carService.getCars().size());
    }

    @Test
    void addDuplicateCarThrowsException() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        Car duplicate = new Car("ABC123", "Honda", 12000.0);

        assertThrows(IllegalArgumentException.class, () -> carService.addCar(duplicate));
    }

    @Test
    void getCarsReturnsAll() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "BMW", 25000.0));

        List<Car> cars = carService.getCars();
        assertEquals(2, cars.size());
    }

    @Test
    void getCarByPlateNumberFound() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        Optional<Car> car = carService.getCarByPlateNumber("ABC123");
        assertTrue(car.isPresent());
        assertEquals("Toyota", car.get().getBrand());
    }

    @Test
    void getCarByPlateNumberNotFound() {
        Optional<Car> car = carService.getCarByPlateNumber("UNKNOWN");
        assertFalse(car.isPresent());
    }

    @Test
    void deleteCarSuccess() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        assertTrue(carService.deleteCar("ABC123"));
        assertEquals(0, carService.getCars().size());
    }

    @Test
    void deleteNonExistentCarReturnsFalse() {
        assertFalse(carService.deleteCar("UNKNOWN"));
    }

    @Test
    void rentCarSetsUnavailable() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        Car rented = carService.rentCar("ABC123");
        assertFalse(rented.isAvailable());
    }

    @Test
    void rentAlreadyRentedCarThrowsException() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.rentCar("ABC123");

        assertThrows(IllegalStateException.class, () -> carService.rentCar("ABC123"));
    }

    @Test
    void rentNonExistentCarThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> carService.rentCar("UNKNOWN"));
    }

    @Test
    void returnCarSetsAvailable() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.rentCar("ABC123");

        Car returned = carService.returnCar("ABC123");
        assertTrue(returned.isAvailable());
    }

    @Test
    void returnAvailableCarThrowsException() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        assertThrows(IllegalStateException.class, () -> carService.returnCar("ABC123"));
    }

    @Test
    void returnNonExistentCarThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> carService.returnCar("UNKNOWN"));
    }
}
