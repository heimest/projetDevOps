package com.example.tpdevops.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

/**
 * Véhicule du parc, identifié par sa plaque d’immatriculation (clé naturelle).
 * La persistance est gérée par JPA; le schéma applicatif est appliqué par Flyway (pas par {@code ddl-auto} create).
 */
@Entity
@Table(name = "cars")
@DynamicUpdate
public class Car {

    @Id
    @Column(name = "plate_number", length = 64, updatable = false, nullable = false)
    private String plateNumber;

    @Column(name = "brand", nullable = false, length = 255)
    private String brand;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "available", nullable = false)
    private boolean available = true;

    public Car() {
    }

    public Car(String plateNumber, String brand, double price) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.price = price;
        this.available = true;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return plateNumber != null && plateNumber.equals(car.plateNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(plateNumber);
    }
}
