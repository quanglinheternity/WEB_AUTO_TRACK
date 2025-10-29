package com.transport.dto.vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.transport.enums.TripStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

public record VehicleAndTripResponse(
    Long id,
    String licensePlate,
    String brand,
    String vehicleTypeName,
    String model,
    String color,
    String vin,
    String engineNumber,
    LocalDate registrationDate,
    LocalDate inspectionExpiryDate,
    LocalDate insuranceExpiryDate,
    Integer manufactureYear,
    String status,
    String note,
    Set<TripResponseSimple> trips,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TripResponseSimple {
        String tripCode;

        // Thông tin cơ bản
        String routeName;
        String vehiclePlateNumber;
        String driverName;

        LocalDateTime departureTime;
        LocalDateTime estimatedArrivalTime;
        LocalDateTime actualArrivalTime;

        TripStatus status;

        String cargoDescription;
        BigDecimal cargoWeight;
    }
}
