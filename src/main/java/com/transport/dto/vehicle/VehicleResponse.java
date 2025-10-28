package com.transport.dto.vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
public class VehicleResponse {
    Long id;
    String licensePlate;
    String brand;
    String vehicleTypeName;
    String model;
    String color;
    String vin;
    String engineNumber;
    LocalDate registrationDate;
    LocalDate inspectionExpiryDate;
    LocalDate insuranceExpiryDate;
    Integer manufactureYear;
    String status;
    String note;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
