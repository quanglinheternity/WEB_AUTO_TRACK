package com.transport.repository.driver;

import java.time.YearMonth;
import java.util.List;

import com.transport.dto.expenseCategory.ExpenseByExpenseCategory;
import com.transport.entity.domain.Driver;

public interface DriverRepositoryCustom {
    boolean existsBylicenseNumber(String licenseNumber);

    List<Driver> findAllActiveDriversExcludePaid(YearMonth month);
    ExpenseByExpenseCategory getExpenseByExpenseCategory(Long driverId, YearMonth month);
}
