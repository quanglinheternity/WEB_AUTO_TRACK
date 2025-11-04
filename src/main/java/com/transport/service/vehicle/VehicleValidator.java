package com.transport.service.vehicle;

import org.springframework.stereotype.Component;

import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleUpdateRequest;
import com.transport.entity.domain.Vehicle;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.vehicle.VehicleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleValidator {
    VehicleRepository vehicleRepository;

    public void validateBeforeCreate(VehicleRequest request) {
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new AppException(ErrorCode.VEHICLE_ALREADY_EXISTS);
        }
        ;
        if (vehicleRepository.existsByVin(request.getVin())) {
            throw new AppException(ErrorCode.VIN_ALREADY_EXISTS);
        }
    }

    public Vehicle validateAndGetExistingVehicle(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
    }

    public Vehicle validateBeforeUpdate(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = validateAndGetExistingVehicle(id);

        if (vehicleRepository.existsByLicensePlateAndIdNot(request.getLicensePlate(), id)) {
            throw new AppException(ErrorCode.VEHICLE_ALREADY_EXISTS);
        }

        if (vehicleRepository.existsByVinAndIdNot(request.getVin(), id)) {
            throw new AppException(ErrorCode.VIN_ALREADY_EXISTS);
        }

        return vehicle;
    }

    public void validateExistence(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new AppException(ErrorCode.VEHICLE_NOT_FOUND);
        }
    }
}
