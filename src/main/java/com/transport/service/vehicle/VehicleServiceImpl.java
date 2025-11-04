package com.transport.service.vehicle;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transport.dto.page.PageResponse;
import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleSearchRequest;
import com.transport.dto.vehicle.VehicleUpdateRequest;
import com.transport.entity.domain.Vehicle;
import com.transport.entity.domain.VehicleType;
import com.transport.enums.VehicleStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.VehicleMapper;
import com.transport.repository.vehicle.VehicleRepository;
import com.transport.repository.vehicleType.VehicleTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class VehicleServiceImpl implements VehicleService {
    VehicleRepository vehicleRepository;
    VehicleTypeRepository vehicleTypeRepository;
    VehicleMapper vehicleMapper;
    VehicleValidator vehicleValidator;

    @Override
    public PageResponse<VehicleResponse> getAll(VehicleSearchRequest request, Pageable pageable) {
        Page<VehicleResponse> page = vehicleRepository.searchVehicles(request, pageable);
        return PageResponse.from(page);
    }

    @Override
    public VehicleResponse create(VehicleRequest request) {
        vehicleValidator.validateBeforeCreate(request);

        Vehicle vehicle = vehicleMapper.toVehicleFromCreateRequest(request);

        VehicleType type = vehicleTypeRepository
                .findById(request.getVehicleTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));
        vehicle.setVehicleType(type);

        vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public VehicleResponse update(Long id, VehicleUpdateRequest request) {

        Vehicle vehicle = vehicleValidator.validateBeforeUpdate(id, request);

        vehicleMapper.updateVehicleFromUpdateRequest(request, vehicle);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public VehicleAndTripResponse getById(Long id) {
        Vehicle vehicle = vehicleValidator.validateAndGetExistingVehicle(id);
        return vehicleMapper.toVehicleDetailResponse(vehicle);
    }

    @Override
    public void delete(Long id) {
        vehicleValidator.validateExistence(id);
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleResponse toggleActiveStatus(Long id) {
        Vehicle vehicle = vehicleValidator.validateAndGetExistingVehicle(id);
        if (vehicle.getStatus() == VehicleStatus.INACTIVE) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
        } else {
            vehicle.setStatus(VehicleStatus.INACTIVE);
        }
        vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponse(vehicle);
    }
}
