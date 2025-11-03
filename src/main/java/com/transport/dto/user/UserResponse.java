package com.transport.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.transport.dto.driver.DriverResponse;

public record UserResponse(
    Long id,
    String username,
    String phone,
    String fullName,
    String idNumber,
    LocalDate dateOfBirth,
    String address,
    String avatarUrl,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    DriverResponse driver
) {

}
