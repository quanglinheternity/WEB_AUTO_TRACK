package com.transport.service.expenseCategory;

import org.springframework.data.domain.Pageable;

import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.dto.expenseCategory.ExpenseCategoryResponse;
import com.transport.dto.expenseCategory.ExpenseCategorySearchRequest;
import com.transport.dto.page.PageResponse;

public interface ExpenseCategoryService {
    PageResponse<ExpenseCategoryResponse> search(ExpenseCategorySearchRequest request, Pageable pageable);

    ExpenseCategoryResponse create(ExpenseCategoryRequest request);

    ExpenseCategoryResponse update(Long id, ExpenseCategoryRequest request);

    void delete(Long id);

    ExpenseCategoryResponse getById(Long id);
}
