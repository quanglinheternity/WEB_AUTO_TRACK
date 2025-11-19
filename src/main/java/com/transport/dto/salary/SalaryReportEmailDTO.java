package com.transport.dto.salary;

import lombok.*;
import java.math.BigDecimal;
import java.time.YearMonth;

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