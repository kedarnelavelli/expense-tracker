package com.assignment.expense_tracker.service;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    ExpenseDTO createExpense(ExpenseDTO dto);

    ExpenseDTO updateExpense(Long id, ExpenseDTO dto);

    List<ExpenseDTO> listExpenses(Optional<String> sortBy);

    List<CategoryTotalDTO> topCategories(int n);
}

