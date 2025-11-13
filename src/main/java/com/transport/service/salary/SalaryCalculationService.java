package com.transport.service.salary;

import java.time.YearMonth;
import java.util.List;

import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.dto.salary.SalaryReportDetailResponse1;

public interface SalaryCalculationService {
    SalaryCalculationResponse calculateSalary(Long driverId, YearMonth month);

    List<SalaryCalculationResponse> calculateSalaryForAllDrivers(YearMonth month);

    void markAsPaid(Long salaryReportId);
    SalaryReportDetailResponse1 calculateSalaryDetail(Long reportId);
}
