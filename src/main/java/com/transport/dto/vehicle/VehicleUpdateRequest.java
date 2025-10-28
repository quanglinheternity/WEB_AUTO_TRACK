package com.transport.dto.vehicle;

import com.transport.enums.VehicleStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class VehicleUpdateRequest {
    // --- Biển số xe ---
    @Size(max = 15, message = "VEHICLE_LICENSE_PLATE_TOO_LONG")
    @Pattern(regexp = "^[0-9A-Z-]+$", message = "VEHICLE_LICENSE_PLATE_INVALID_FORMAT")
    String licensePlate;

    // --- Loại xe ---
    Long vehicleTypeId;

    // --- Thông tin cơ bản ---
    @Size(max = 50, message = "VEHICLE_NAME_TOO_LONG")
    String brand;

    @Size(max = 50, message = "VEHICLE_MODEL_TOO_LONG")
    String model;

    @Min(value = 1990, message = "VEHICLE_MANUFACTURE_YEAR_INVALID")
    @Max(value = 2025, message = "VEHICLE_MANUFACTURE_YEAR_INVALID")
    Integer manufactureYear;

    @Size(max = 30, message = "VEHICLE_COLOR_TOO_LONG")
    String color;

    @Size(max = 50, message = "VEHICLE_CHASSIS_NUMBER_TOO_LONG")
    String vin;

    @Size(max = 50, message = "VEHICLE_ENGINE_NUMBER_TOO_LONG")
    String engineNumber;

    // --- Ngày bảo hiểm & đăng kiểm ---
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "VEHICLE_INSPECTION_DATE_INVALID")
    String registrationDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "VEHICLE_INSURANCE_DATE_INVALID")
    String inspectionExpiryDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "VEHICLE_INSURANCE_DATE_INVALID")
    String insuranceExpiryDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "VEHICLE_PURCHASE_DATE_INVALID")
    String purchaseDate;

    VehicleStatus status;
    String note;
}
