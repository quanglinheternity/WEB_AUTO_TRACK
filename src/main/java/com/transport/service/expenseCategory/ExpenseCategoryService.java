package com.transport.service.expenseCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.dto.expenseCategory.ExpenseCategoryResponse;


public interface ExpenseCategoryService {
    Page<ExpenseCategoryResponse> search(String keyword, Pageable pageable);

    ExpenseCategoryResponse create(ExpenseCategoryRequest request);

    ExpenseCategoryResponse update(Long id, ExpenseCategoryRequest request);

    void delete(Long id);

    ExpenseCategoryResponse getById(Long id);
}
