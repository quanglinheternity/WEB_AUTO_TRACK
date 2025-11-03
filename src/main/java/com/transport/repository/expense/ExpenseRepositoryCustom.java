package com.transport.repository.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.expense.ExpenseSearchRequest;
import com.transport.entity.domain.Expense;

public interface ExpenseRepositoryCustom {
    Page<Expense> searchExpenses(ExpenseSearchRequest request, Pageable pageable);
}
