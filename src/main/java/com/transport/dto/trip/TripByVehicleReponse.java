package com.transport.dto.trip;

import java.time.LocalDateTime;

public record TripByVehicleReponse(
        String driverCode,
        String driverName,
        String licensePlate,
        String model,
        LocalDateTime departureTime,
        String origin,
        String destination,
        Long totalTrip,
        String note) {}
