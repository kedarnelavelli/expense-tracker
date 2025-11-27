package com.assignment.expense_tracker.repository;

import com.assignment.expense_tracker.entity.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category ORDER BY SUM(e.amount) DESC")
    List<Object[]> findTopCategoriesByTotalAmount(Pageable pageable);
}
