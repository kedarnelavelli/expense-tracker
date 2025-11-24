package com.assignment.expense_tracker.controller;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@Valid @RequestBody ExpenseDTO dto) {
        log.info("Creating expense: {}", dto);
        ExpenseDTO created = expenseService.createExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> list(@RequestParam(required=false) String sortBy) {
        log.info("Fetching expenses, sortBy = {}", sortBy);
        return ResponseEntity.ok(expenseService.listExpenses(Optional.ofNullable(sortBy)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> update(@PathVariable Long id,
                                             @Valid @RequestBody ExpenseDTO dto) {
        log.info("Updating expense id={} with data={}", id, dto);
        return ResponseEntity.ok(expenseService.updateExpense(id, dto));
    }

    @GetMapping("/top")
    public ResponseEntity<List<CategoryTotalDTO>> top() {
        log.info("Fetching top 3 categories");
        return ResponseEntity.ok(expenseService.topCategories(3));
    }
}

