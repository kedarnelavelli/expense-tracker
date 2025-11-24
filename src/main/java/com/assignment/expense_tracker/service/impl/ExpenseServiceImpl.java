package com.assignment.expense_tracker.service.impl;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.entity.Expense;
import com.assignment.expense_tracker.exception.BadRequestException;
import com.assignment.expense_tracker.exception.ResourceNotFoundException;
import com.assignment.expense_tracker.repository.ExpenseRepository;
import com.assignment.expense_tracker.service.ExpenseService;
import com.assignment.expense_tracker.util.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repo;

    @Override
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        validate(dto);
        Expense saved = repo.save(ExpenseMapper.toEntity(dto));
        return ExpenseMapper.toDTO(saved);
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto) {
        validate(dto);

        Expense existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + id));

        existing.setCategory(dto.getCategory());
        existing.setAmount(dto.getAmount());
        if (dto.getDate() != null) {
            existing.setDate(java.time.LocalDateTime.parse(dto.getDate()));
        }
        existing.setNotes(dto.getNotes());

        Expense updated = repo.save(existing);
        return ExpenseMapper.toDTO(updated);
    }

    @Override
    public List<ExpenseDTO> listExpenses(Optional<String> sortBy) {

        String sortField = sortBy.orElse("date");

        if (!sortField.equals("date") && !sortField.equals("amount")) {
            sortField = "date";
        }

        Sort sort = Sort.by(Sort.Direction.DESC, sortField);

        return repo.findAll(sort)
                .stream()
                .map(ExpenseMapper::toDTO)
                .toList();
    }

    @Override
    public List<CategoryTotalDTO> topCategories(int n) {
        return repo.findCategoryTotals()
                .stream()
                .limit(n)
                .map(row -> new CategoryTotalDTO(
                        (String) row[0],
                        (java.math.BigDecimal) row[1]
                ))
                .toList();
    }


    private void validate(ExpenseDTO dto) {
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be positive");
        }
        if (dto.getCategory() == null || dto.getCategory().isBlank()) {
            throw new BadRequestException("Category is required");
        }
        String date = dto.getDate();
        if (date != null && !date.isBlank()) {
            String isoRegex = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$";

            if (!date.matches(isoRegex)) {
                throw new BadRequestException("Date must be in ISO format: yyyy-MM-dd'T'HH:mm:ss");
            }
        } else {
            throw new BadRequestException("Date is required");
        }
    }
}
