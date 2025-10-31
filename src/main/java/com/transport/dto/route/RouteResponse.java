package com.transport.dto.route;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;


public record RouteResponse( 
    Long id,
    String code,
    String name,
    String origin,
    String destination,
    BigDecimal distanceKm,
    BigDecimal estimatedDurationHours,
    String description,
    BigDecimal estimatedFuelCost,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Boolean isActive
    ) {
    @JsonProperty("isActive")
    public String getIsActiveDisplay() {
        return Boolean.TRUE.equals(isActive) ? "Hoạt động" : "Không hoạt động";
    }

    
}
