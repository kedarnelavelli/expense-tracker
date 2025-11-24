package com.assignment.expense_tracker.service;

import com.assignment.expense_tracker.dto.CategoryTotalDTO;
import com.assignment.expense_tracker.dto.ExpenseDTO;
import com.assignment.expense_tracker.entity.Expense;
import com.assignment.expense_tracker.exception.BadRequestException;
import com.assignment.expense_tracker.exception.ResourceNotFoundException;
import com.assignment.expense_tracker.repository.ExpenseRepository;
import com.assignment.expense_tracker.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                .category("Food")
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
                .category("Food")
                .amount(BigDecimal.valueOf(100))
                .build();

        assertThatThrownBy(() -> service.updateExpense(1L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createExpense_success() {
        ExpenseDTO dto = ExpenseDTO.builder()
                .category("Food")
                .amount(BigDecimal.valueOf(200))
                .date("2025-11-22T10:00:00")
                .build();

        Expense saved = Expense.builder()
                .id(1L)
                .category("Food")
                .amount(BigDecimal.valueOf(200))
                .date(LocalDateTime.parse("2025-11-22T10:00:00"))
                .build();

        when(repo.save(any())).thenReturn(saved);

        ExpenseDTO result = service.createExpense(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCategory()).isEqualTo("Food");
        assertThat(result.getAmount()).isEqualTo("200");
    }

    @Test
    void updateExpense_success() {
        Long id = 1L;

        Expense existing = Expense.builder()
                .id(id)
                .category("Food")
                .amount(BigDecimal.valueOf(100))
                .date(LocalDateTime.parse("2025-11-22T10:00:00"))
                .notes("old")
                .build();

        ExpenseDTO dto = ExpenseDTO.builder()
                .category("Travel")
                .amount(BigDecimal.valueOf(300))
                .date("2025-11-22T12:00:00")
                .notes("updated")
                .build();

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Expense.class))).thenAnswer(inv -> inv.getArgument(0));

        ExpenseDTO result = service.updateExpense(id, dto);

        assertThat(result.getCategory()).isEqualTo("Travel");
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(300));
        assertThat(result.getNotes()).isEqualTo("updated");
    }

    @Test
    void listExpenses_sortedByAmount() {
        Expense e1 = Expense.builder()
                .id(1L).category("A")
                .amount(BigDecimal.valueOf(200))
                .date(LocalDateTime.now())
                .build();

        Expense e2 = Expense.builder()
                .id(2L).category("B")
                .amount(BigDecimal.valueOf(500))
                .date(LocalDateTime.now())
                .build();

        when(repo.findAll(Sort.by(Sort.Direction.DESC, "amount")))
                .thenReturn(List.of(e2, e1));

        List<ExpenseDTO> result =
                service.listExpenses(Optional.of("amount"));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAmount())
                .isEqualTo(BigDecimal.valueOf(500));
        assertThat(result.get(1).getAmount())
                .isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void topCategories_returnsDTOList() {

        List<Object[]> totals = List.of(
                new Object[]{"Food", BigDecimal.valueOf(600)},
                new Object[]{"Travel", BigDecimal.valueOf(400)},
                new Object[]{"Grocery", BigDecimal.valueOf(200)},
                new Object[]{"Misc", BigDecimal.valueOf(100)}
        );

        when(repo.findCategoryTotals()).thenReturn(totals);

        List<CategoryTotalDTO> result = service.topCategories(3);

        assertThat(result).hasSize(3);

        assertThat(result.get(0).category()).isEqualTo("Food");
        assertThat(result.get(0).total()).isEqualTo(BigDecimal.valueOf(600));

        assertThat(result.get(1).category()).isEqualTo("Travel");
        assertThat(result.get(1).total()).isEqualTo(BigDecimal.valueOf(400));

        assertThat(result.get(2).category()).isEqualTo("Grocery");
        assertThat(result.get(2).total()).isEqualTo(BigDecimal.valueOf(200));
    }

}

