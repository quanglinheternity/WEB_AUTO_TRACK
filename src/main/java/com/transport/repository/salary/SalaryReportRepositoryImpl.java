package com.transport.repository.salary;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.dto.salary.SalaryReportSearchRequest;
import com.transport.entity.domain.QDriver;
import com.transport.entity.domain.QSalaryReport;
import com.transport.entity.domain.QUser;
import com.transport.entity.domain.SalaryReport;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SalaryReportRepositoryImpl implements SalaryReportRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QSalaryReport salaryReport = QSalaryReport.salaryReport;
    private final QDriver driver = QDriver.driver;
    private final QUser user = QUser.user;

    @Override
    public boolean existsByDriverIdAndReportMonth(Long driverId, YearMonth reportMonth) {
        if (driverId == null || reportMonth == null) return false;

        // Giả sử field trong DB là LocalDate hoặc YearMonth bạn lưu tháng đó

        Integer count = queryFactory
                .selectOne()
                .from(salaryReport)
                .where(salaryReport.driver.id.eq(driverId), salaryReport.reportMonth.eq(reportMonth))
                .fetchFirst();

        return count != null;
    }

    @Override
    public List<SalaryReport> findAllByIdIn(List<Long> reportIds) {
        if (reportIds == null || reportIds.isEmpty()) return List.of();

        return queryFactory
                .selectFrom(salaryReport)
                .where(salaryReport.id.in(reportIds))
                .fetch();
    }

    @Override
    public List<SalaryReport> findAllByReportMonth(YearMonth month) {
        if (month == null) return List.of();

        return queryFactory
                .selectFrom(salaryReport)
                .where(salaryReport.reportMonth.eq(month))
                .fetch();
    }

    @Override
    public Page<SalaryCalculationResponse> searchSalaryReports(SalaryReportSearchRequest request, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // --- Bộ lọc tháng ---
        if (request.getMonth() != null) {
            builder.and(salaryReport.reportMonth.eq(request.getMonth()));
        }

        // --- Bộ lọc theo tài xế ---
        if (request.getDriverId() != null) {
            builder.and(salaryReport.driver.id.eq(request.getDriverId()));
        }

        // --- Bộ lọc theo trạng thái thanh toán ---
        if (request.getIsPaid() != null) {
            builder.and(salaryReport.isPaid.eq(request.getIsPaid()));
        }

        // --- Tìm kiếm theo keyword ---
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            String keyword = request.getKeyword().trim();
            builder.and(salaryReport
                    .driver
                    .user
                    .fullName
                    .containsIgnoreCase(keyword)
                    .or(salaryReport.note.containsIgnoreCase(keyword)));
        }

        // --- Khởi tạo query ---
        var query = queryFactory
                .selectFrom(salaryReport)
                .leftJoin(salaryReport.driver, driver)
                .fetchJoin()
                .leftJoin(driver.user, user)
                .fetchJoin()
                .where(builder);

        // --- Sort ---
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "reportMonth" -> query.orderBy(
                            order.isAscending() ? salaryReport.reportMonth.asc() : salaryReport.reportMonth.desc());
                    case "totalSalary" -> query.orderBy(
                            order.isAscending() ? salaryReport.totalSalary.asc() : salaryReport.totalSalary.desc());
                    case "isPaid" -> query.orderBy(
                            order.isAscending() ? salaryReport.isPaid.asc() : salaryReport.isPaid.desc());
                    default -> query.orderBy(salaryReport.createdAt.desc());
                }
            });
        } else {
            query.orderBy(salaryReport.createdAt.desc());
        }

        // --- Tổng số bản ghi ---
        Long count = queryFactory
                .select(salaryReport.count())
                .from(salaryReport)
                .where(builder)
                .fetchOne();
        long total = (count != null) ? count : 0L;
        // --- Kết quả ---
        List<SalaryCalculationResponse> results =
                query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch().stream()
                        .map(report -> SalaryCalculationResponse.builder()
                                .driverId(report.getDriver().getId())
                                .driverName(report.getDriver().getUser().getFullName())
                                .driverCode(report.getDriver().getDriverCode())
                                .month(report.getReportMonth())
                                .baseSalary(report.getBaseSalary())
                                .totalTrips(report.getTotalTrips())
                                .totalDistance(report.getTotalDistance())
                                .tripBonus(report.getTripBonus())
                                .allowance(report.getAllowance())
                                .deduction(report.getDeduction())
                                .totalSalary(report.getTotalSalary())
                                .note(report.getNote())
                                .build())
                        .toList();

        return new PageImpl<>(results, pageable, total);
    }
}
