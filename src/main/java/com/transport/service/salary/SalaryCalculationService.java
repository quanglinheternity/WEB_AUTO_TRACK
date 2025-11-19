package com.transport.service.salary;

import java.time.YearMonth;
import java.util.List;

import com.transport.dto.salary.SalaryCalculationDetailResponse;
import com.transport.dto.salary.SalaryCalculationResponse;

public interface SalaryCalculationService {
    SalaryCalculationResponse calculateSalary(Long driverId, YearMonth month);

    List<SalaryCalculationResponse> calculateSalaryForAllDrivers(YearMonth month);

    void markAsPaid(Long salaryReportId);

    SalaryCalculationDetailResponse calculateSalaryDetail(Long reportId);

    void sendSalaryReportsForMonth(YearMonth month);

    void sendSalaryReportEmail(Long reportId);
}
