package com.transport.dto.driver;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.transport.enums.EmploymentStatus;
import com.transport.validation.ValidEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverRequest {
    @NotBlank(message = "DRIVER_LICENSE_NUMBER_EMPTY")
    private String licenseNumber;

    @NotBlank(message = "DRIVER_LICENSE_TYPE_EMPTY")
    private String licenseClass;

    @NotNull(message = "DRIVER_LICENSE_ISSUE_DATE_EMPTY")
    private Date licenseIssueDate;

    @NotNull(message = "DRIVER_LICENSE_EXPIRY_DATE_EMPTY")
    private Date licenseExpiryDate;

    @NotNull(message = "DRIVER_EXPERIENCE_EMPTY")
    @Min(value = 0, message = "DRIVER_EXPERIENCE_INVALID")
    private Integer yearsOfExperience;

    @NotNull(message = "DRIVER_STATUS_EMPTY")
    @ValidEnum(enumClass = EmploymentStatus.class, message = "DRIVER_STATUS_INVALID")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "DRIVER_SALARY_EMPTY")
    @DecimalMin(value = "0.0", inclusive = false, message = "DRIVER_SALARY_INVALID")
    private BigDecimal baseSalary;

    private String note;
}
