package com.example.tpdevops.services;

import com.example.tpdevops.entities.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CarService {

    private final Map<String, Car> cars = new HashMap<>();

    public Car addCar(Car car) {
        if (cars.containsKey(car.getPlateNumber())) {
            throw new IllegalArgumentException("Une voiture avec la plaque " + car.getPlateNumber() + " existe déjà");
        }
        cars.put(car.getPlateNumber(), car);
        return car;
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars.values());
    }

    public Optional<Car> getCarByPlateNumber(String plateNumber) {
        return Optional.ofNullable(cars.get(plateNumber));
    }

    public boolean deleteCar(String plateNumber) {
        return cars.remove(plateNumber) != null;
    }

    public Car rentCar(String plateNumber) {
        Car car = cars.get(plateNumber);
        if (car == null) {
            throw new IllegalArgumentException("Voiture introuvable : " + plateNumber);
        }
        if (!car.isAvailable()) {
            throw new IllegalStateException("La voiture est déjà louée : " + plateNumber);
        }
        car.setAvailable(false);
        return car;
    }

    public Car returnCar(String plateNumber) {
        Car car = cars.get(plateNumber);
        if (car == null) {
            throw new IllegalArgumentException("Voiture introuvable : " + plateNumber);
        }
        if (car.isAvailable()) {
            throw new IllegalStateException("La voiture n'est pas en location : " + plateNumber);
        }
        car.setAvailable(true);
        return car;
    }

    public void clearAll() {
        cars.clear();
    }
}