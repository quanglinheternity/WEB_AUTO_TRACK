package com.transport.repository.trip;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.trip.TripSearchRequest;
import com.transport.entity.domain.Trip;

public interface TripRepositoryCustom {
    long countOverlappingTripsByDriver(Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime);
    long countOverlappingTripsByVehicle(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime);
    long countOverlappingTripsByVehicleExcluding(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId);
    long countOverlappingTripsByDriverExcluding(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId);
    Page<Trip> searchTrips(TripSearchRequest request, Pageable pageable);
}
