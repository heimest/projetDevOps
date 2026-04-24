package com.example.tpdevops.repositories;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.entities.RentalRecord;
import com.example.tpdevops.entities.RentalStatus;
import com.example.tpdevops.testsupport.AbstractPostgresIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RentalRecordRepositoryIT extends AbstractPostgresIT {

    @Autowired
    private RentalRecordRepository rentalRecordRepository;

    @Test
    void findByCar_PlateNumberAndStatus() {
        Car car = carRepository.save(new Car("R1", "Volvo", 1.0));
        RentalRecord r = new RentalRecord();
        r.setCar(car);
        r.setCustomerName("Pat");
        r.setStartDate(java.time.LocalDateTime.now());
        r.setTotalPrice(10.0);
        r.setStatus(RentalStatus.ACTIVE);
        rentalRecordRepository.save(r);

        Optional<RentalRecord> found = rentalRecordRepository.findByCar_PlateNumberAndStatus("R1", RentalStatus.ACTIVE);
        assertTrue(found.isPresent());
        assertEquals(10.0, found.get().getTotalPrice());
    }
}
