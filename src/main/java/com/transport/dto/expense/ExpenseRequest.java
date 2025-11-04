package com.transport.dto.expense;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpenseRequest {
    @NotNull(message = "COST_TRIP_ID_EMPTY")
    Long tripId;

    @NotNull(message = "COST_GROUP_EMPTY")
    Long categoryId;

    @NotNull(message = "COST_AMOUNT_EMPTY")
    @DecimalMin(value = "0.0", inclusive = false, message = "COST_AMOUNT_INVALID")
    BigDecimal amount;

    @NotNull(message = "COST_DATE_EMPTY")
    Date expenseDate;

    @Size(max = 255, message = "COST_LOCATION_TOO_LONG")
    String location;

    String description;
}
