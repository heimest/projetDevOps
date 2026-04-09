package com.example.tpdevops.controllers;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class RentServiceRestController {

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        try {
            return ResponseEntity.ok(carService.addCar(car));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Car> getCars() {
        return carService.getCars();
    }

    @GetMapping("/{plateNumber}")
    public ResponseEntity<Car> getCarByPlateNumber(@PathVariable String plateNumber) {
        return carService.getCarByPlateNumber(plateNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{plateNumber}")
    public ResponseEntity<Void> deleteCar(@PathVariable String plateNumber) {
        if (carService.deleteCar(plateNumber)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{plateNumber}/rent")
    public ResponseEntity<Car> rentCar(@PathVariable String plateNumber) {
        try {
            return ResponseEntity.ok(carService.rentCar(plateNumber));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{plateNumber}/return")
    public ResponseEntity<Car> returnCar(@PathVariable String plateNumber) {
        try {
            return ResponseEntity.ok(carService.returnCar(plateNumber));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}