package com.transport.dto.expenseCategory;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseByExpenseCategory {
    String nameDriver;
    List<ExpenseCategory> expenseCategories;
    BigDecimal total;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseCategory {
        private String name;
        private String desciption;
        private BigDecimal amount;
    }
}
