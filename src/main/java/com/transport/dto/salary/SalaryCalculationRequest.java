package com.transport.dto.salary;

import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryCalculationRequest {
    private Long driverId;
    private YearMonth month; // Format: "2024-11"
}
