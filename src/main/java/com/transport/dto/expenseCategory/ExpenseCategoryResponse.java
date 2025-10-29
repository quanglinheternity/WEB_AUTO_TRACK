package com.transport.dto.expenseCategory;



public record ExpenseCategoryResponse(
    Long id,
    String code,
    String name,
    String description,
    String categoryGroup,
    Boolean isActive
) {
}
