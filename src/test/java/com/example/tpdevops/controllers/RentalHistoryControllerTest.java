package com.example.tpdevops.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RentalHistoryControllerTest {

    @Autowired
    private WebApplicationContext context;

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
        mockMvc.perform(post("/rentals/start")
                        .param("plateNumber", "XYZ999")
                        .param("customerName", "Charlie")
                        .param("dailyPrice", "75.0"))
                .andExpect(status().isOk());
    }

    @Test
    void testEndRentalNotFound() throws Exception {
        mockMvc.perform(put("/rentals/end/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEndRental() throws Exception {
        mockMvc.perform(post("/rentals/start")
                        .param("plateNumber", "END123")
                        .param("customerName", "Dave")
                        .param("dailyPrice", "60.0"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/rentals/end/END123"))
                .andExpect(status().isOk());
    }
}