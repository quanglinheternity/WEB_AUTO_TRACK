package com.transport.repository.salary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.SalaryReport;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface SalaryReportRepository extends JpaRepository<SalaryReport, Long>, SalaryReportRepositoryCustom {
    List<SalaryReport> findByReportMonth(YearMonth month);
}
