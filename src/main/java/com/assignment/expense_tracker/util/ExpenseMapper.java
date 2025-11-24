package com.assignment.expense_tracker.util;

import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.entity.Expense;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpenseMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public static Expense toEntity(ExpenseDTO dto) {
        return Expense.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .amount(dto.getAmount())
                .date(dto.getDate() != null
                        ? LocalDateTime.parse(dto.getDate(), FORMATTER)
                        : LocalDateTime.now())
                .notes(dto.getNotes())
                .build();
    }

    public static ExpenseDTO toDTO(Expense entity) {
        return ExpenseDTO.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .amount(entity.getAmount())
                .date(entity.getDate().format(FORMATTER))
                .notes(entity.getNotes())
                .build();
    }
}

