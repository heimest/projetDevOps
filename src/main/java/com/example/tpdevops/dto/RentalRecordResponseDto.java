package com.example.tpdevops.dto;

import com.example.tpdevops.entities.RentalStatus;

import java.time.LocalDateTime;

/**
 * Vue de l’historique de location pour l’API HTTP (hors entité JPA / graphe d’Hibernate).
 */
public record RentalRecordResponseDto(
    Long id,
    String plateNumber,
    String customerName,
    LocalDateTime startDate,
    LocalDateTime endDate,
    double totalPrice,
    RentalStatus status
) {
}
