package com.transport.dto.vehicle;

import java.time.LocalDate;

import com.transport.enums.VehicleStatus;

import lombok.Data;

@Data
public class VehicleSearchRequest {
    private String keyword; // tìm theo biển số, model, brand, vin
    private VehicleStatus status; // lọc theo trạng thái
    private Long vehicleTypeId; // lọc theo loại xe
    private LocalDate registrationDateFrom;
    private LocalDate registrationDateTo;
}
