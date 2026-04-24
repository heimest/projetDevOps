package com.example.tpdevops.dto;

import com.example.tpdevops.entities.RentalRecord;

/**
 * Bord d’API pour les fiches de location, sans exposer l’entité de persistance.
 */
public final class RentalRecordDtoMapper {

    private RentalRecordDtoMapper() {
    }

    public static RentalRecordResponseDto toResponse(RentalRecord record) {
        return new RentalRecordResponseDto(
            record.getId(),
            record.getPlateNumber(),
            record.getCustomerName(),
            record.getStartDate(),
            record.getEndDate(),
            record.getTotalPrice(),
            record.getStatus()
        );
    }
}
