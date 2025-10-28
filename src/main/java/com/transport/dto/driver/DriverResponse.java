package com.transport.dto.driver;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.transport.enums.EmploymentStatus;

public record DriverResponse(
    Long id,
    String driverCode,
    String licenseNumber,
    String licenseClass,
    LocalDate licenseIssueDate,
    LocalDate licenseExpiryDate,
    Integer yearsOfExperience,
    EmploymentStatus employmentStatus,
    BigDecimal baseSalary,
    String note
) {

}
