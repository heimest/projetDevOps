package com.example.tpdevops.dto;

/**
 * Donnée d'entrée pour la création d'un véhicule : seuls les champs autorisés côté client
 * (évite l'assignation de masse sur l'entité JPA, p.ex. forcer {@code available}).
 */
public record CarRequestDto(String plateNumber, String brand, double price) {
}
