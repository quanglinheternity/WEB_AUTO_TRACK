package com.transport.dto.expense;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseReportResponse(
        List<ExpenseByTripResponse> expenses, BigDecimal totalEstimatedFuelCost, BigDecimal totalResidual) {}
