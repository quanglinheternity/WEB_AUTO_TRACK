package com.transport.repository.driver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.expenseCategory.ExpenseByExpenseCategory;
import com.transport.entity.domain.Driver;
import com.transport.entity.domain.QDriver;
import com.transport.entity.domain.QExpense;
import com.transport.entity.domain.QExpenseCategory;
import com.transport.entity.domain.QSalaryReport;
import com.transport.enums.EmploymentStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DriverRepositoryImpl implements DriverRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QDriver qDriver = QDriver.driver;
    private final QSalaryReport salaryReport = QSalaryReport.salaryReport;
    private final QExpense expense = QExpense.expense;
    private final QExpenseCategory expenseCategory = QExpenseCategory.expenseCategory;
    private final QDriver driver = QDriver.driver;

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
    @Override
    public ExpenseByExpenseCategory getExpenseByExpenseCategory(Long driverId, YearMonth month) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();

        // Lấy tất cả category, kể cả driver chưa có chi phí
        List<Tuple> results = queryFactory
                .select(
                        expenseCategory.name,
                        expenseCategory.description,
                        expense.amount.sum().coalesce(BigDecimal.ZERO)
                )
                .from(expenseCategory)
                .leftJoin(expense).on(
                        expense.category.id.eq(expenseCategory.id)
                        .and(expense.driverBy.id.eq(driverId))
                        .and(expense.expenseDate.between(startOfMonth, endOfMonth))
                )
                .groupBy(expenseCategory.id, expenseCategory.name, expenseCategory.description)
                .fetch();

        // Chuyển kết quả thành danh sách ExpenseCategory
        List<ExpenseByExpenseCategory.ExpenseCategory> categories = results.stream()
                .map(t -> new ExpenseByExpenseCategory.ExpenseCategory(
                        t.get(expenseCategory.name),
                        t.get(expenseCategory.description),
                        t.get(expense.amount.sum().coalesce(BigDecimal.ZERO))
                ))
                .toList();

        // Lấy tên driver (nếu không có chi phí, cần fetch driver riêng)
        String driverName = queryFactory
                .select(driver.user.fullName)
                .from(driver)
                .where(driver.id.eq(driverId))
                .fetchOne();
        BigDecimal total = categories.stream()
            .map(ExpenseByExpenseCategory.ExpenseCategory::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseByExpenseCategory(driverName, categories, total);
    }

}
