package com.transport.dto.route;

import java.math.BigDecimal;

public record RouteBySalary(String name, Long totalTrips, BigDecimal totalDistance, BigDecimal totalSalary) {}
