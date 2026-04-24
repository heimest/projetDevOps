package com.example.tpdevops.entities;

/**
 * Statut métier d’une fiche de location, persisté en VARCHAR côté PostgreSQL (voir
 * <code>CHECK</code> en migration V1) pour un schéma auditable et sans surprise.
 */
public enum RentalStatus {
    /** La voiture est considérée en cours de location. */
    ACTIVE,
    /** La location a été clôturée. */
    COMPLETED
}
