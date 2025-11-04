package com.transport.service.trip;

import java.time.LocalDateTime;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transport.dto.page.PageResponse;
import com.transport.dto.trip.ApproveTripRequest;
import com.transport.dto.trip.TripCreateRequest;
import com.transport.dto.trip.TripResponse;
import com.transport.dto.trip.TripSearchRequest;
import com.transport.dto.trip.TripUpdateRequest;
import com.transport.dto.trip.UpdateTripStatusRequest;
import com.transport.entity.domain.Driver;
import com.transport.entity.domain.Route;
import com.transport.entity.domain.Trip;
import com.transport.entity.domain.User;
import com.transport.entity.domain.Vehicle;
import com.transport.enums.TripStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.TripMapper;
import com.transport.repository.trip.TripRepository;
import com.transport.service.authentication.auth.AuthenticationService;
import com.transport.util.CodeGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TripServiceImpl implements TripService {
    TripRepository tripRepository;
    TripMapper tripMapper;
    TripValidator tripValidator;
    AuthenticationService authenticationService;

    @Override
    public PageResponse<TripResponse> getAll(TripSearchRequest request, Pageable pageable) {
        // return nguoiDungRepository.findAll();
        User currentUser = authenticationService.getCurrentUser();
        if (!authenticationService.hasPermission("TRIP_READ")) {
            request.setDriverId(currentUser.getId());
        }
        Page<Trip> page = tripRepository.searchTrips(request, pageable);

        return PageResponse.from(page.map(tripMapper::toTripResponse));
    }

    @Override
    public TripResponse createTrip(TripCreateRequest request) {
        User user = authenticationService.getCurrentUser();
        Route route = tripValidator.validateRoute(request.getRouteId());
        Vehicle vehicle = tripValidator.validateVehicle(request.getVehicleId());
        tripValidator.validateVehicleOverlap(
                vehicle.getId(), request.getDepartureTime(), request.getEstimatedArrivalTime());
        Driver driver = tripValidator.validateDriver(request.getDriverId());
        tripValidator.validateDriverOverlap(
                driver.getId(), request.getDepartureTime(), request.getEstimatedArrivalTime());
        tripValidator.validateCargoWeight(vehicle.getId(), request.getCargoWeight());
        Trip trip = tripMapper.toCreateTrip(request);
        trip.setTripCode(CodeGenerator.generateCode("LT"));
        trip.setCreatedBy(user);
        trip.setStatus(TripStatus.NOT_STARTED);
        // trip.setApprovedByUser(user);
        trip.setRoute(route);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        return tripMapper.toTripResponse(tripRepository.save(trip));
    }

    @Override
    public TripResponse updateTrip(Long id, TripUpdateRequest request) {
        // User user = authenticationService.getCurrentUser();

        Trip trip = tripRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TRIP_NOT_FOUND));
        if (trip.getStatus() != TripStatus.NOT_STARTED) {
            throw new AppException(ErrorCode.ONLY_NOT_STARTED_TRIP_CAN_BE_UPDATED);
        }
        Route route = tripValidator.validateRoute(request.getRouteId());
        Vehicle vehicle = tripValidator.validateVehicle(request.getVehicleId());
        Driver driver = tripValidator.validateDriver(request.getDriverId());

        tripValidator.validateVehicleOverlapExcluding(
                vehicle.getId(), request.getDepartureTime(), request.getEstimatedArrivalTime(), id);
        tripValidator.validateDriverOverlapExcluding(
                driver.getId(), request.getDepartureTime(), request.getEstimatedArrivalTime(), id);

        tripValidator.validateCargoWeight(vehicle.getId(), request.getCargoWeight());

        tripMapper.updateFromRequest(request, trip);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setRoute(route);
        trip.setStatus(TripStatus.NOT_STARTED);

        return tripMapper.toTripResponse(tripRepository.save(trip));
    }

    @Override
    public TripResponse updateTripStatus(Long id, UpdateTripStatusRequest request) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TRIP_NOT_FOUND));

        TripStatus newStatus = request.getNewStatus();
        tripValidator.validateTripStatus(trip, newStatus);
        if (newStatus == TripStatus.CANCELLED
                && (request.getNote() == null || request.getNote().isBlank())) {
            throw new AppException(ErrorCode.CANCELLATION_REASON_REQUIRED);
        }
        switch (newStatus) {
            case ARRIVED -> {
                trip.setActualArrivalTime(LocalDateTime.now());
                trip.setNote(request.getNote());
            }
            case CANCELLED -> {
                trip.setCancelledByUser(authenticationService.getCurrentUser());
                trip.setCancellationReason(request.getNote());
                trip.setCancelledAt(LocalDateTime.now());
                trip.setNote(null);
            }
            default -> {
                trip.setNote(request.getNote());
            }
        }

        trip.setStatus(newStatus);
        tripRepository.save(trip);

        return tripMapper.toTripResponse(trip);
    }

    @Override
    public TripResponse approveTrip(Long id, ApproveTripRequest request) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TRIP_NOT_FOUND));
        tripValidator.validateTripForApprove(trip);

        boolean isApproved = Boolean.TRUE.equals(request.getApproved());
        String rejectionReason = request.getReason();

        if (!isApproved && (rejectionReason == null || rejectionReason.isBlank())) {
            throw new AppException(ErrorCode.REJECTION_REASON_REQUIRED);
        }

        User approver = authenticationService.getCurrentUser();
        trip.setApprovedByUser(approver);
        trip.setApprovedAt(LocalDateTime.now());
        trip.setApprovalStatus(isApproved);
        trip.setNote(isApproved ? null : rejectionReason);
        trip.setUpdatedAt(LocalDateTime.now());

        tripRepository.save(trip);
        return tripMapper.toTripResponse(trip);
    }

    @Override
    public void delete(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TRIP_NOT_FOUND));
        if (trip.getStatus() != TripStatus.NOT_STARTED) {
            throw new AppException(ErrorCode.ONLY_NOT_STARTED_TRIP_CAN_BE_DELETED);
        }
        tripRepository.delete(trip);
    }
}
