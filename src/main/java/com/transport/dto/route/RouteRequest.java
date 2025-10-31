package com.transport.dto.route;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RouteRequest {
    @NotBlank(message = "ROUTE_NAME_EMPTY")
    private String name;

    @NotBlank(message = "ROUTE_START_LOCATION_EMPTY")
    private String origin;

    @NotBlank(message = "ROUTE_END_LOCATION_EMPTY")
    private String destination;

    @NotNull(message = "ROUTE_DISTANCE_EMPTY")
    @DecimalMin(value = "0.1", message = "ROUTE_DISTANCE_INVALID")
    private BigDecimal distanceKm;

    @NotNull(message = "ROUTE_EXPECTED_TIME_INVALID")
    private BigDecimal estimatedDurationHours;

    private String description;

    @NotNull(message = "ROUTE_EXPECTED_COST_EMPTY")
    @DecimalMin(value = "0", inclusive = true, message = "ROUTE_EXPECTED_COST_INVALID")
    private BigDecimal estimatedFuelCost;

    
}
