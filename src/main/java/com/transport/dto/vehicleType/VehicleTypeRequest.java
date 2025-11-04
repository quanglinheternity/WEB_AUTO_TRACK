package com.transport.dto.vehicleType;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTypeRequest {

    @NotBlank(message = "VEHICLE_TYPE_NAME_EMPTY")
    @Size(max = 100, message = "VEHICLE_TYPE_NAME_TOO_LONG")
    private String name;

    @NotNull(message = "VEHICLE_TYPE_WEIGHT_EMPTY")
    @DecimalMin(value = "0.0", inclusive = false, message = "VEHICLE_TYPE_WEIGHT_INVALID")
    private BigDecimal maxPayload;

    @Size(max = 500, message = "VEHICLE_TYPE_DESCRIPTION_TOO_LONG")
    private String description;

    private Boolean isActive;
}
