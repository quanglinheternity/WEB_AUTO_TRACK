package com.transport.service.vehicleType;

import org.springframework.stereotype.Component;

import com.transport.dto.vehicleType.VehicleTypeRequest;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.vehicleType.VehicleTypeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleTypeValidator {

    private final VehicleTypeRepository vehicleTypeRepository;

    public void validateBeforeCreate(VehicleTypeRequest request) {
        if (vehicleTypeRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.VEHICLE_TYPE_ALREADY_EXISTS);
        }
    }

    public void validateBeforeUpdate(Long id, VehicleTypeRequest request) {
        if (vehicleTypeRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.VEHICLE_TYPE_ALREADY_EXISTS);
        }
    }
}
