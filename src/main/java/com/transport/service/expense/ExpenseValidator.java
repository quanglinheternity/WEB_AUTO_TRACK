package com.transport.service.expense;

import org.springframework.stereotype.Component;

import com.transport.entity.domain.Expense;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.entity.domain.Trip;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.expense.ExpenseRepository;
import com.transport.service.expenseCategory.ExpenseCategoryValidator;
import com.transport.service.trip.TripValidator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpenseValidator {
    ExpenseRepository expenseRepository;
    TripValidator tripValidator;
    ExpenseCategoryValidator expenseCategoryValidator;
    public Trip validateTrip(Long tripId) {
       return tripValidator.validateTrip(tripId);
    }

    public ExpenseCategory validateExpenseCategory(Long expenseCategoryId) {
       return expenseCategoryValidator.validateCategoryById(expenseCategoryId);
    }
    public Expense validateExpense(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EXPENSE_NOT_FOUND));
    }
}
