package com.example.tpdevops.controllers;

import com.example.tpdevops.entities.Car;
import com.example.tpdevops.services.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RentServiceRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CarService carService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        carService.clearAll(); // repart d'un état propre avant chaque test
    }

    @Test
    public void testAddCar() throws Exception {
        Car car = new Car("ABC123", "Toyota", 15000.0);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("ABC123"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void testAddDuplicateCarReturnsBadRequest() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Car("ABC123", "Honda", 12000.0))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCars() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "BMW", 25000.0));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetCarByPlateNumber() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        mockMvc.perform(get("/cars/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.price").value(15000.0));
    }

    @Test
    public void testGetCarByPlateNumberNotFound() throws Exception {
        mockMvc.perform(get("/cars/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCar() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        mockMvc.perform(delete("/cars/ABC123"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCarNotFound() throws Exception {
        mockMvc.perform(delete("/cars/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRentCar() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        mockMvc.perform(put("/cars/ABC123/rent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    public void testRentCarNotFound() throws Exception {
        mockMvc.perform(put("/cars/UNKNOWN/rent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRentAlreadyRentedCarReturnsBadRequest() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.rentCar("ABC123");

        mockMvc.perform(put("/cars/ABC123/rent"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testReturnCar() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.rentCar("ABC123");

        mockMvc.perform(put("/cars/ABC123/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void testReturnCarNotFound() throws Exception {
        mockMvc.perform(put("/cars/UNKNOWN/return"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReturnAvailableCarReturnsBadRequest() throws Exception {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));

        mockMvc.perform(put("/cars/ABC123/return"))
                .andExpect(status().isBadRequest());
    }
}