package com.example.tpdevops.dto;

/**
 * Vue exposée par l'API (pas d'entité JPA) : l'état réel (disponible, etc.) vient du domaine,
 * le client ne recolle pas l'entité telle quelle au JSON d'entrée.
 */
public record CarResponseDto(
    String plateNumber,
    String brand,
    double price,
    boolean available
) {
}
