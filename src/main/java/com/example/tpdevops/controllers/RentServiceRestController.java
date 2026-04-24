package com.example.tpdevops.controllers;

import com.example.tpdevops.dto.CarDtoMapper;
import com.example.tpdevops.dto.CarRequestDto;
import com.example.tpdevops.dto.CarResponseDto;
import com.example.tpdevops.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class RentServiceRestController {

    private final CarService carService;

    public RentServiceRestController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<CarResponseDto> addCar(@RequestBody CarRequestDto car) {
        try {
            return ResponseEntity.ok(CarDtoMapper.toResponse(carService.addCar(CarDtoMapper.toEntity(car))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<CarResponseDto> getCars() {
        return carService.getCars().stream().map(CarDtoMapper::toResponse).toList();
    }

    @GetMapping("/{plateNumber}")
    public ResponseEntity<CarResponseDto> getCarByPlateNumber(@PathVariable String plateNumber) {
        return carService.getCarByPlateNumber(plateNumber)
            .map(CarDtoMapper::toResponse)
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
    public ResponseEntity<CarResponseDto> rentCar(@PathVariable String plateNumber) {
        try {
            return ResponseEntity.ok(CarDtoMapper.toResponse(carService.rentCar(plateNumber)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{plateNumber}/return")
    public ResponseEntity<CarResponseDto> returnCar(@PathVariable String plateNumber) {
        try {
            return ResponseEntity.ok(CarDtoMapper.toResponse(carService.returnCar(plateNumber)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
