package com.assignment.expense_tracker.dto;

import com.assignment.expense_tracker.enums.ExpenseCategory;

import java.math.BigDecimal;

public record CategoryTotalDTO(ExpenseCategory category, BigDecimal total) {}

