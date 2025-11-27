package com.assignment.expense_tracker.service;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.entity.Expense;
import com.assignment.expense_tracker.enums.ExpenseCategory;
import com.assignment.expense_tracker.enums.ExpenseSortField;
import com.assignment.expense_tracker.exception.BadRequestException;
import com.assignment.expense_tracker.exception.ResourceNotFoundException;
import com.assignment.expense_tracker.repository.ExpenseRepository;
import com.assignment.expense_tracker.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository repo;

    @InjectMocks
    private ExpenseServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExpense_invalidAmount_throwsBadRequest() {
        ExpenseDTO dto = ExpenseDTO.builder()
                .category(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(-10))
                .build();

        assertThatThrownBy(() -> service.createExpense(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void updateExpense_notFound_throwsException() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        ExpenseDTO dto = ExpenseDTO.builder()
                .id(1L)
                .category(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(100))
                .date(OffsetDateTime.now())
                .build();

        assertThatThrownBy(() -> service.updateExpense(dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createExpense_success() {
        ExpenseDTO dto = ExpenseDTO.builder()
                .category(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(200))
                .date(OffsetDateTime.now())
                .build();

        Expense saved = Expense.builder()
                .id(1L)
                .category(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(200))
                .date(OffsetDateTime.now())
                .build();

        when(repo.save(any())).thenReturn(saved);

        ExpenseDTO result = service.createExpense(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCategory()).isEqualTo(ExpenseCategory.FOOD);
        assertThat(result.getAmount()).isEqualTo("200");
    }

    @Test
    void updateExpense_success() {
        Long id = 1L;

        Expense existing = Expense.builder()
                .id(id)
                .category(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(100))
                .date(OffsetDateTime.now())
                .notes("old")
                .build();

        ExpenseDTO dto = ExpenseDTO.builder()
                .id(id)
                .category(ExpenseCategory.TRAVEL)
                .amount(BigDecimal.valueOf(300))
                .date(OffsetDateTime.now().minusDays(1))
                .notes("updated")
                .build();

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Expense.class))).thenAnswer(inv -> inv.getArgument(0));

        ExpenseDTO result = service.updateExpense(dto);

        assertThat(result.getCategory()).isEqualTo(ExpenseCategory.TRAVEL);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(300));
        assertThat(result.getNotes()).isEqualTo("updated");
    }

    @Test
    void listExpenses_sortedByAmount() {
        Expense e1 = Expense.builder()
                .id(1L).category(ExpenseCategory.TRAVEL)
                .amount(BigDecimal.valueOf(200))
                .date(OffsetDateTime.now())
                .build();

        Expense e2 = Expense.builder()
                .id(2L).category(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(500))
                .date(OffsetDateTime.now())
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "amount"));

        Page<Expense> mockPage = new PageImpl<>(List.of(e2, e1), pageable, 2);

        when(repo.findAll(pageable)).thenReturn(mockPage);

        Page<ExpenseDTO> page =
                service.listExpenses(0, 10, ExpenseSortField.AMOUNT);

        List<ExpenseDTO> result = page.toList();

        assertThat(page).hasSize(2);
        assertThat(result.get(0).getAmount())
                .isEqualTo(BigDecimal.valueOf(500));
        assertThat(result.get(1).getAmount())
                .isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void topCategories_returnsDTOList() {

        List<Object[]> totals = List.of(
                new Object[]{ExpenseCategory.FOOD, BigDecimal.valueOf(600)},
                new Object[]{ExpenseCategory.TRAVEL, BigDecimal.valueOf(400)},
                new Object[]{ExpenseCategory.SHOPPING, BigDecimal.valueOf(200)},
                new Object[]{ExpenseCategory.OTHER, BigDecimal.valueOf(100)}
        );

        Pageable pageable = PageRequest.of(0, 3);

        when(repo.findTopCategoriesByTotalAmount(pageable)).thenReturn(totals);

        List<CategoryTotalDTO> result = service.topCategories(3);

        assertThat(result).hasSize(3);

        assertThat(result.get(0).category()).isEqualTo(ExpenseCategory.FOOD);
        assertThat(result.get(0).total()).isEqualTo(BigDecimal.valueOf(600));

        assertThat(result.get(1).category()).isEqualTo(ExpenseCategory.TRAVEL);
        assertThat(result.get(1).total()).isEqualTo(BigDecimal.valueOf(400));

        assertThat(result.get(2).category()).isEqualTo(ExpenseCategory.SHOPPING);
        assertThat(result.get(2).total()).isEqualTo(BigDecimal.valueOf(200));
    }

}

