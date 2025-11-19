package com.transport.dto.salary;

import java.math.BigDecimal;
import java.time.YearMonth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryReportEmailDTO {
    private String driverName;
    private String driverEmail;
    private YearMonth reportMonth;
    private Integer totalTrips;
    private BigDecimal totalDistance;
    private BigDecimal baseSalary;
    private BigDecimal tripBonus;
    private BigDecimal allowance;
    private BigDecimal deduction;
    private BigDecimal totalSalary;
    private String note;
}
