package com.transport.dto.expense;

import java.math.BigDecimal;

public record ExpenseByExpenseCategoryReponse(String nameCategory, BigDecimal amount) {}
