package com.transport.dto.expenseCategory;

import com.transport.enums.ExpenseGroup;

import lombok.Data;

@Data
public class ExpenseCategorySearchRequest {
    private String keyword;
    private ExpenseGroup group;
    private Boolean isActive;
}
