package com.transport.dto.salary;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.transport.dto.route.RouteBySalary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryCalculationDetailResponse {
    private Long reportId;
    private Long driverId;
    private String driverName;
    private String driverCode;
    private YearMonth month;

    private Integer totalTrips;
    private BigDecimal totalIncome;
    private BigDecimal totalDeductions;
    private BigDecimal netSalary;
    private Map<String, BigDecimal> incomes;
    private List<RouteBySalary> salaryByroutes;
    private Map<String, BigDecimal> deductions;

    private Boolean isPaid;
    private LocalDateTime paidAt;
    private String note;
}
