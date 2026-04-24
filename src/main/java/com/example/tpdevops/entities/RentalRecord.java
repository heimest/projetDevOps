package com.example.tpdevops.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Fiche de location, reliée en base à un véhicule (FK {@code car_plate}).
 * L’exposition JSON expose {@code plateNumber} dérivé de la voiture, sans fuite du graphe
 * d’Hibernate; la relation côté persistance reste explicite pour les jointures.
 */
@Entity
@Table(name = "rental_records", schema = "public")
@DynamicUpdate
public class RentalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(
        name = "car_plate",
        referencedColumnName = "plate_number",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_rental_car")
    )
    private Car car;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private RentalStatus status;

    public RentalRecord() {
    }

    /**
     * Construction domaine (tests, fixtures) : non persisté tant qu’on n’appelle pas
     * {@code repository.save} dans un service transactionnel.
     */
    public RentalRecord(Car car, String customerName, LocalDateTime startDate) {
        this.car = car;
        this.customerName = customerName;
        this.startDate = startDate;
        this.status = RentalStatus.ACTIVE;
        this.totalPrice = 0.0;
    }

    @JsonProperty("plateNumber")
    public String getPlateNumber() {
        return car == null ? null : car.getPlateNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Car getCar() {
        return car;
    }

    @JsonIgnore
    public void setCar(Car car) {
        this.car = car;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        RentalRecord that = (RentalRecord) o;
        if (id == null) {
            return false;
        }
        if (that.id == null) {
            return false;
        }
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
