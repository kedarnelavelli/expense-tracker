package com.assignment.expense_tracker.controller;

import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ExpenseRepository repo;

    @BeforeEach
    void cleanDatabase() {
        repo.deleteAll();
    }


    @Test
    void createExpense_returns201() throws Exception {
        ExpenseDTO dto = ExpenseDTO.builder()
                .category("Food")
                .amount(BigDecimal.valueOf(150))
                .date("2025-11-22T09:00:00")
                .build();

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.amount").value(150));
    }

    @Test
    void getExpenses_afterPost_returnsList() throws Exception {

        ExpenseDTO dto = ExpenseDTO.builder()
                .category("Bills")
                .amount(BigDecimal.valueOf(900))
                .date("2025-11-22T09:00:00")
                .build();

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Bills"));
    }

    @Test
    void getTopCategories_returnsSortedTotals() throws Exception {

        ExpenseDTO e1 = ExpenseDTO.builder()
                .category("Food")
                .amount(BigDecimal.valueOf(300))
                .date("2025-11-22T10:00:00")
                .build();

        ExpenseDTO e2 = ExpenseDTO.builder()
                .category("Travel")
                .amount(BigDecimal.valueOf(200))
                .date("2025-11-22T11:00:00")
                .build();

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(e1)));

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(e2)));

        mockMvc.perform(get("/api/expenses/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Food"))
                .andExpect(jsonPath("$[0].total").value(300));

    }

    @Test
    void createExpense_invalidAmount_returns400() throws Exception {

        ExpenseDTO dto = ExpenseDTO.builder()
                .category("Food")
                .amount(BigDecimal.valueOf(-50))
                .build();

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

}

