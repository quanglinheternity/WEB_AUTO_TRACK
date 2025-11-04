package com.transport.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class TripCreateRequest {
    @NotNull(message = "SCHEDULE_ROUTE_EMPTY")
    Long routeId;

    @NotNull(message = "SCHEDULE_VEHICLE_EMPTY")
    Long vehicleId;

    @NotNull(message = "SCHEDULE_DRIVER_ID_EMPTY")
    Long driverId;

    @NotNull(message = "SCHEDULE_DEPARTURE_TIME_EMPTY")
    @Future(message = "SCHEDULE_DEPARTURE_TIME_INVALID")
    LocalDateTime departureTime;

    @NotNull(message = "SCHEDULE_EXPECTED_ARRIVAL_TIME_EMPTY")
    @Future(message = "SCHEDULE_EXPECTED_ARRIVAL_TIME_INVALID")
    LocalDateTime estimatedArrivalTime;

    @Size(max = 255, message = "CARGO_DESCRIPTION_TOO_LONG")
    String cargoDescription;

    @Positive(message = "CARGO_WEIGHT_INVALID")
    BigDecimal cargoWeight;
}
