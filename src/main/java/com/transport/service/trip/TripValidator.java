package com.transport.service.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.transport.entity.domain.Driver;
import com.transport.entity.domain.Route;
import com.transport.entity.domain.Trip;
import com.transport.entity.domain.Vehicle;
import com.transport.enums.EmploymentStatus;
import com.transport.enums.TripStatus;
import com.transport.enums.VehicleStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.driver.DriverRepository;
import com.transport.repository.trip.TripRepository;
import com.transport.repository.vehicle.VehicleRepository;
import com.transport.service.route.RouteValidator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TripValidator {
    TripRepository tripRepository;
    RouteValidator routeValidator;
    DriverRepository driverRepository;
    VehicleRepository vehicleRepository;

    public Trip validateTrip(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    public Route validateRoute(Long routeId) {
        Route route = routeValidator.validateRoute(routeId);
        return route;
    }

    public Vehicle validateVehicle(Long vehicleId) {
        Vehicle vehicle =
                vehicleRepository.findById(vehicleId).orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE && vehicle.getStatus() != VehicleStatus.IN_USE) {
            throw new AppException(ErrorCode.VEHICLE_INACTIVE);
        }
        return vehicle;
    }

    public void validateVehicleOverlap(
            Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime) {
        Long countOverlappingTrips =
                tripRepository.countOverlappingTripsByVehicle(vehicleId, departureTime, estimatedArrivalTime);
        if (countOverlappingTrips > 0) {
            throw new AppException(ErrorCode.SCHEDULE_VEHICLE_ALREADY_BOOKED);
        }
    }

    public void validateDriverOverlap(Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime) {
        Long countOverlappingTrips =
                tripRepository.countOverlappingTripsByDriver(driverId, departureTime, estimatedArrivalTime);
        if (countOverlappingTrips > 0) {
            throw new AppException(ErrorCode.SCHEDULE_DRIVER_ALREADY_BOOKED);
        }
    }

    public void validateVehicleOverlapExcluding(
            Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId) {
        Long countOverlappingTrips = tripRepository.countOverlappingTripsByVehicleExcluding(
                vehicleId, departureTime, estimatedArrivalTime, excludeTripId);
        if (countOverlappingTrips > 0) {
            throw new AppException(ErrorCode.SCHEDULE_VEHICLE_ALREADY_BOOKED);
        }
    }

    public void validateDriverOverlapExcluding(
            Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId) {
        Long countOverlappingTrips = tripRepository.countOverlappingTripsByDriverExcluding(
                driverId, departureTime, estimatedArrivalTime, excludeTripId);
        if (countOverlappingTrips > 0) {
            throw new AppException(ErrorCode.SCHEDULE_DRIVER_ALREADY_BOOKED);
        }
    }

    public Driver validateDriver(Long taiXeId) {
        Driver driver =
                driverRepository.findById(taiXeId).orElseThrow(() -> new AppException(ErrorCode.DRIVER_NOT_FOUND));
        if (driver.getEmploymentStatus() != EmploymentStatus.ACTIVE) {
            throw new AppException(ErrorCode.DRIVER_INACTIVE);
        }
        return driver;
    }

    public void validateCargoWeight(Long vehicleId, BigDecimal cargoWeight) {
        if (cargoWeight == null) {
            throw new AppException(ErrorCode.CARGO_WEIGHT_REQUIRED);
        }

        BigDecimal maxPayloadTon = vehicleRepository.findMaxPayloadByVehicleId(vehicleId);
        if (maxPayloadTon == null) {
            throw new AppException(ErrorCode.VEHICLE_PAYLOAD_NOT_CONFIGURED);
        }
        BigDecimal cargoWeightTon = cargoWeight.divide(BigDecimal.valueOf(1000));
        if (cargoWeightTon.compareTo(cargoWeightTon) > 0) {
            throw new AppException(ErrorCode.CARGO_OVER_WEIGHT);
        }
    }

    public void validateTripForApprove(Trip trip) {
        if (trip.getStatus() != TripStatus.ARRIVED) {
            throw new AppException(ErrorCode.TRIP_NOT_ARRIVED);
        }

        if (trip.getApprovalStatus() != null) {
            throw new AppException(ErrorCode.TRIP_ALREADY_REVIEWED);
        }
    }

    public void validateTripStatus(Trip trip, TripStatus newStatus) {
        TripStatus currentStatus = trip.getStatus();

        switch (newStatus) {
            case IN_PROGRESS -> {
                if (currentStatus != TripStatus.NOT_STARTED && currentStatus != TripStatus.PAUSED) {
                    throw new AppException(ErrorCode.INVALID_TRIP_STATUS_TRANSITION);
                }
            }
            case PAUSED -> {
                if (currentStatus != TripStatus.IN_PROGRESS) {
                    throw new AppException(ErrorCode.INVALID_TRIP_STATUS_TRANSITION);
                }
            }
            case ARRIVED -> {
                if (currentStatus != TripStatus.IN_PROGRESS) {
                    throw new AppException(ErrorCode.INVALID_TRIP_STATUS_TRANSITION);
                }
            }
            case CANCELLED -> {
                if (currentStatus == TripStatus.ARRIVED) {
                    throw new AppException(ErrorCode.CANNOT_CANCEL_COMPLETED_TRIP);
                }
            }
            default -> {
                // Allow NOT_STARTED without restriction
            }
        }
    }
    public void validateVehicleCanBeDeleted(Long vehicleId) {
        if(tripRepository.isVehicleUsedInTrip(vehicleId)) {
            throw new AppException(ErrorCode.VEHICLE_IN_USE);
        }
    }
}
