package com.transport.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TripResponse(
        Long id,
        String tripCode,

        // Thông tin cơ bản
        String routeName,
        String vehiclePlateNumber,
        String driverName,
        LocalDateTime departureTime,
        LocalDateTime estimatedArrivalTime,
        LocalDateTime actualArrivalTime,
        String status,
        String cargoDescription,
        BigDecimal cargoWeight,

        // Người tạo & phê duyệt
        String createdBy,
        LocalDateTime approvedAt,
        String approvedBy,
        LocalDateTime completedAt,
        LocalDateTime cancelledAt,
        String cancellationReason,
        String note) {}
