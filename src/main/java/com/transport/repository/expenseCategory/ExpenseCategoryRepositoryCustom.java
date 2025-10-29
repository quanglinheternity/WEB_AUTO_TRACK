package com.transport.repository.expenseCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.entity.domain.ExpenseCategory;

public interface ExpenseCategoryRepositoryCustom {
    Page<ExpenseCategory> search(String keyword, Pageable pageable);

    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Long id);
}
