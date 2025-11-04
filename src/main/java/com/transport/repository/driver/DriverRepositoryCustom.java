package com.transport.repository.driver;

import java.time.YearMonth;
import java.util.List;

import com.transport.entity.domain.Driver;

public interface DriverRepositoryCustom {
    boolean existsBylicenseNumber(String licenseNumber);

    List<Driver> findAllActiveDriversExcludePaid(YearMonth month);
}
