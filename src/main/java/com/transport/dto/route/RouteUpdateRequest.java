package com.transport.dto.route;

import java.math.BigDecimal;

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
public class RouteUpdateRequest {

    private String name; // optional, nếu muốn đổi tên
    private String origin; // optional
    private String destination; // optional
    private BigDecimal distanceKm; // optional
    private BigDecimal estimatedDurationHours; // optional
    private String description; // optional
    private BigDecimal estimatedFuelCost; // optional
    private Boolean isActive; // optional, true = Hoạt động, false = Ngưng hoạt động
}
