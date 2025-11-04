package com.transport.repository.driver;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.entity.domain.Driver;
import com.transport.entity.domain.QDriver;
import com.transport.entity.domain.QSalaryReport;
import com.transport.enums.EmploymentStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DriverRepositoryImpl implements DriverRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QDriver qDriver = QDriver.driver;
    private final QSalaryReport salaryReport = QSalaryReport.salaryReport;

    @Override
    public boolean existsBylicenseNumber(String licenseNumber) {
        Integer count = queryFactory
                .selectOne()
                .from(qDriver)
                .where(qDriver.licenseNumber.eq(licenseNumber))
                .fetchFirst();

        return count != null;
    }

    @Override
    public List<Driver> findAllActiveDriversExcludePaid(YearMonth month) {
        return queryFactory
                .selectFrom(qDriver)
                .where(
                        qDriver.employmentStatus.eq(EmploymentStatus.ACTIVE),
                        qDriver.id.notIn(JPAExpressions.select(salaryReport.driver.id)
                                .from(salaryReport)
                                .where(salaryReport.reportMonth.eq(month))))
                .fetch();
    }
}
