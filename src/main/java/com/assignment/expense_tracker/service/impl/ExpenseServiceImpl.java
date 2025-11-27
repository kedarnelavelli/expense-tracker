package com.assignment.expense_tracker.service.impl;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.entity.Expense;
import com.assignment.expense_tracker.enums.ExpenseCategory;
import com.assignment.expense_tracker.enums.ExpenseSortField;
import com.assignment.expense_tracker.exception.BadRequestException;
import com.assignment.expense_tracker.exception.ResourceNotFoundException;
import com.assignment.expense_tracker.repository.ExpenseRepository;
import com.assignment.expense_tracker.service.ExpenseService;
import com.assignment.expense_tracker.util.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public ExpenseDTO updateExpense(ExpenseDTO dto) {
        validate(dto);
        validateId(dto);
        Expense existing = repo.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + dto.getId()));

        existing.setCategory(dto.getCategory());
        existing.setAmount(dto.getAmount());
        existing.setDate(dto.getDate());
        existing.setNotes(dto.getNotes());

        Expense updated = repo.save(existing);
        return ExpenseMapper.toDTO(updated);
    }

    @Override
    public Page<ExpenseDTO> listExpenses(int page, int size, ExpenseSortField sortField) {

        Pageable pageable = PageRequest.of(page, size, buildSort(sortField));

        return repo.findAll(pageable)
                .map(ExpenseMapper::toDTO);
    }

    private Sort buildSort(ExpenseSortField sortField) {
        return switch (sortField) {
            case DATE -> Sort.by(Sort.Direction.DESC, "date");
            case AMOUNT -> Sort.by(Sort.Direction.DESC, "amount");
            default -> Sort.unsorted();
        };
    }

    @Override
    public List<CategoryTotalDTO> topCategories(int n) {
        Pageable pageable = PageRequest.of(0, n);

        return repo.findTopCategoriesByTotalAmount(pageable)
                .stream()
                .limit(n)
                .map(row -> new CategoryTotalDTO(
                        (ExpenseCategory) row[0],
                        (java.math.BigDecimal) row[1]
                ))
                .toList();
    }


    private void validate(ExpenseDTO dto) {
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be positive");
        }
        if (dto.getCategory() == null) {
            throw new BadRequestException("Category is required");
        }
        if(dto.getDate() == null) {
            throw new BadRequestException("Date is required");
        }
    }

    private void validateId(ExpenseDTO dto) {
        if (dto.getId() == null) {
            throw new BadRequestException("Id is required");
        }
    }
}
