package com.assignment.expense_tracker.util;

import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.entity.Expense;

public class ExpenseMapper {


    public static Expense toEntity(ExpenseDTO dto) {
        return Expense.builder()
                .category(dto.getCategory())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .notes(dto.getNotes())
                .build();
    }

    public static ExpenseDTO toDTO(Expense entity) {
        return ExpenseDTO.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .notes(entity.getNotes())
                .build();
    }
}

