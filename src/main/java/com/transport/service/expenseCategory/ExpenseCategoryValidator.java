package com.transport.service.expenseCategory;

import org.springframework.stereotype.Component;

import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.expenseCategory.ExpenseCategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExpenseCategoryValidator {
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public void validateBeforeCreate(ExpenseCategoryRequest request) {
        if (expenseCategoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.EXPENSE_TYPE_ALREADY_EXISTS);
        }
    }

    public void validateBeforeUpdate(Long id, ExpenseCategoryRequest request) {
        if (expenseCategoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.EXPENSE_TYPE_ALREADY_EXISTS);
        }
        
    }
    public ExpenseCategory getExpenseCategory(Long id) {
        return expenseCategoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EXPENSE_TYPE_NOT_FOUND));
    }
}
