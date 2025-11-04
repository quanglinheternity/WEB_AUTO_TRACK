package com.transport.repository.salary;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.dto.salary.SalaryReportSearchRequest;
import com.transport.entity.domain.SalaryReport;

public interface SalaryReportRepositoryCustom {
    boolean existsByDriverIdAndReportMonth(Long driverId, YearMonth reportMonth);

    List<SalaryReport> findAllByIdIn(List<Long> reportIds);

    List<SalaryReport> findAllByReportMonth(YearMonth month);

    Page<SalaryCalculationResponse> searchSalaryReports(SalaryReportSearchRequest request, Pageable pageable);
}
