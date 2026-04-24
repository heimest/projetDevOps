package com.example.tpdevops.services;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Métier du parc voitures, isolé des contrôleurs HTTP. La persistance est assurée par
 * la couche repository, transactionnelle pour garder l’invariant d’exclusivité des plaques.
 */
@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional(readOnly = true)
    public List<Car> getCars() {
        return carRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Car> getCarByPlateNumber(String plateNumber) {
        return carRepository.findById(plateNumber);
    }

    public Car addCar(Car car) {
        if (carRepository.existsById(car.getPlateNumber())) {
            throw new IllegalArgumentException(
                "Une voiture avec la plaque " + car.getPlateNumber() + " existe déjà"
            );
        }
        return carRepository.save(car);
    }

    public boolean deleteCar(String plateNumber) {
        if (carRepository.existsById(plateNumber)) {
            carRepository.deleteById(plateNumber);
            return true;
        }
        return false;
    }

    public Car rentCar(String plateNumber) {
        Car c = carRepository
            .findById(plateNumber)
            .orElseThrow(() -> new IllegalArgumentException("Voiture introuvable : " + plateNumber));
        if (!c.isAvailable()) {
            throw new IllegalStateException("La voiture est déjà louée : " + plateNumber);
        }
        c.setAvailable(false);
        return carRepository.save(c);
    }

    public Car returnCar(String plateNumber) {
        Car c = carRepository
            .findById(plateNumber)
            .orElseThrow(() -> new IllegalArgumentException("Voiture introuvable : " + plateNumber));
        if (c.isAvailable()) {
            throw new IllegalStateException("La voiture n'est pas en location : " + plateNumber);
        }
        c.setAvailable(true);
        return carRepository.save(c);
    }
}
