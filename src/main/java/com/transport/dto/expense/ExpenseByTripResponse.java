package com.transport.dto.expense;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseByTripResponse(
        String driverCode,
        String driverName,
        String licensePlate,
        String model,
        BigDecimal estimatedFuelCost,
        List<ExpenseByExpenseCategoryReponse> expenseCategory,
        BigDecimal residual) {}
