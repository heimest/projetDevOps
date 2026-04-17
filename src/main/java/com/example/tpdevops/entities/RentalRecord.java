package com.example.tpdevops.entities;

import java.time.LocalDateTime;

public class RentalRecord {

    private String plateNumber;
    private String customerName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalPrice;
    private String status;

    public RentalRecord(String plateNumber, String customerName, LocalDateTime startDate) {
        this.plateNumber = plateNumber;
        this.customerName = customerName;
        this.startDate = startDate;
        this.status = "ACTIVE";
    }

    public String getPlateNumber() { return plateNumber; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}