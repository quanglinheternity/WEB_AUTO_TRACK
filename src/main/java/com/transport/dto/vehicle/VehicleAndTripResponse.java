package com.transport.dto.vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.transport.dto.trip.TripResponse;

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
    Set<TripResponse> trips,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
