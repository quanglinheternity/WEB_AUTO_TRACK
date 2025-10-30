package com.transport.repository.trip;

import java.time.LocalDateTime;

public interface TripRepositoryCustom {
    long countOverlappingTripsByDriver(Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime);
    long countOverlappingTripsByVehicle(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime);
    long countOverlappingTripsByVehicleExcluding(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId);
    long countOverlappingTripsByDriverExcluding(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId);
}
