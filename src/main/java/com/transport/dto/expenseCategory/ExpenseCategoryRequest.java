package com.transport.dto.expenseCategory;

import com.transport.enums.ExpenseGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCategoryRequest {
    @NotBlank(message = "COST_TYPE_NAME_EMPTY")
    String name;

    String description;

    @NotNull(message = "COST_GROUP_EMPTY")
    ExpenseGroup categoryGroup;
    @NotNull(message = "VEHICLE_TYPE_STATUS_EMPTY")
    Boolean isActive;
}
