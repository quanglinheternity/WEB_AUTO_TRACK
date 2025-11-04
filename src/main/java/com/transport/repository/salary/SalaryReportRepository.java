package com.transport.repository.salary;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.SalaryReport;

@Repository
public interface SalaryReportRepository extends JpaRepository<SalaryReport, Long>, SalaryReportRepositoryCustom {
    
}
