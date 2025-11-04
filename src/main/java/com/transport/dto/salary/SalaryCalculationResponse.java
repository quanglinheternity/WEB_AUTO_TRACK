package com.transport.dto.salary;

import java.math.BigDecimal;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryCalculationResponse {
    private Long driverId;
    private String driverName;
    private String driverCode;
    private YearMonth month;

    // Chi tiết tính toán
    private BigDecimal baseSalary;
    private Integer totalTrips;
    private BigDecimal totalDistance;
    private BigDecimal tripBonus;
    private BigDecimal allowance;
    private BigDecimal deduction;
    private BigDecimal totalSalary;

    private String note;
}
