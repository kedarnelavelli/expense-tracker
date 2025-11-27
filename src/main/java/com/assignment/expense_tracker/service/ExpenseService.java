package com.assignment.expense_tracker.service;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.enums.ExpenseSortField;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExpenseService {

    ExpenseDTO createExpense(ExpenseDTO dto);

    ExpenseDTO updateExpense(ExpenseDTO dto);

    Page<ExpenseDTO> listExpenses(int page, int size, ExpenseSortField sortField);

    List<CategoryTotalDTO> topCategories(int n);
}

