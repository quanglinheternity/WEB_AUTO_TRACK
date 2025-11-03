package com.transport.repository.expenseCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.expenseCategory.ExpenseCategorySearchRequest;
import com.transport.entity.domain.ExpenseCategory;

public interface ExpenseCategoryRepositoryCustom {
    Page<ExpenseCategory> searchExpenseCategories(ExpenseCategorySearchRequest request, Pageable pageable);

    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Long id);
}
