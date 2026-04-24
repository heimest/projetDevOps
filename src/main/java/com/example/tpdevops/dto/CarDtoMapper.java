package com.example.tpdevops.dto;

import com.example.tpdevops.entities.Car;

/**
 * Mapping bord d'API ↔ domaine, sans fuite d'entités dans les contrôleurs HTTP.
 */
public final class CarDtoMapper {

    private CarDtoMapper() {
    }

    public static Car toEntity(CarRequestDto dto) {
        return new Car(dto.plateNumber(), dto.brand(), dto.price());
    }

    public static CarResponseDto toResponse(Car car) {
        return new CarResponseDto(
            car.getPlateNumber(),
            car.getBrand(),
            car.getPrice(),
            car.isAvailable()
        );
    }
}
