package com.example.tpdevops.controllers;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.services.CarService;
import com.example.tpdevops.testsupport.AbstractPostgresIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RentalHistoryControllerTest extends AbstractPostgresIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CarService carService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetAllRecords() throws Exception {
        mockMvc.perform(get("/rentals"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetActiveRentals() throws Exception {
        mockMvc.perform(get("/rentals/active"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetRecordsByCustomer() throws Exception {
        mockMvc.perform(get("/rentals/customer/Alice"))
            .andExpect(status().isOk());
    }

    @Test
    void testStartRental() throws Exception {
        carService.addCar(new Car("XYZ999", "Ford", 10_000.0));

        mockMvc.perform(post("/rentals/start")
                .param("plateNumber", "XYZ999")
                .param("customerName", "Charlie")
                .param("dailyPrice", "75.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerName").value("Charlie"));
    }

    @Test
    void testStartRentalNotFound() throws Exception {
        mockMvc.perform(post("/rentals/start")
                .param("plateNumber", "NOCAR")
                .param("customerName", "A")
                .param("dailyPrice", "1.0"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testStartRentalDuplicateActive() throws Exception {
        carService.addCar(new Car("DUP1", "Skoda", 1.0));
        mockMvc.perform(post("/rentals/start")
            .param("plateNumber", "DUP1")
            .param("customerName", "A")
            .param("dailyPrice", "1.0"))
            .andExpect(status().isOk());
        mockMvc.perform(post("/rentals/start")
            .param("plateNumber", "DUP1")
            .param("customerName", "B")
            .param("dailyPrice", "2.0"))
            .andExpect(status().isConflict());
    }

    @Test
    void testEndRentalNotFound() throws Exception {
        mockMvc.perform(put("/rentals/end/UNKNOWN"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testEndRental() throws Exception {
        carService.addCar(new Car("END123", "Kia", 1.0));
        mockMvc.perform(post("/rentals/start")
                .param("plateNumber", "END123")
                .param("customerName", "Dave")
                .param("dailyPrice", "60.0"))
            .andExpect(status().isOk());

        mockMvc.perform(put("/rentals/end/END123"))
            .andExpect(status().isOk());
    }
}
