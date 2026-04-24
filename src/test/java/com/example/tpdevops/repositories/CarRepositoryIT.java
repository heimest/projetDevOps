package com.example.tpdevops.repositories;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.testsupport.AbstractPostgresIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRepositoryIT extends AbstractPostgresIT {

    @Autowired
    private CarRepository carRepository;

    @Test
    void saveAndReadById() {
        Car c = new Car("X1", "Fiat", 120.0);
        carRepository.save(c);
        assertEquals(1, carRepository.findAll().size());
        assertEquals(120.0, carRepository.findById("X1").orElseThrow().getPrice());
    }

    @Test
    void exampleQueryMatcher() {
        carRepository.save(new Car("A", "B", 1.0));
        Car probe = new Car();
        probe.setBrand("B");
        Example<Car> example = Example.of(
            probe,
            ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("price", "available")
        );
        List<Car> match = carRepository.findAll(example);
        assertEquals(1, match.size());
        assertEquals("A", match.get(0).getPlateNumber());
    }
}
