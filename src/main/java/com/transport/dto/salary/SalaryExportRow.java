package com.transport.dto.salary;

import java.math.BigDecimal;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalaryExportRow {
    private String routeName;
    private Long totalTrips;
    private BigDecimal totalDistance;
    private BigDecimal routeIncome;

    private String driverCode;
    private String driverName;
    private YearMonth month;

    private BigDecimal baseSalary;
    private BigDecimal jobAllowance;
    private BigDecimal insurance;
    private BigDecimal unionFee;

    private BigDecimal totalIncome;
    private BigDecimal totalDeductions;
    private BigDecimal netSalary;
}
