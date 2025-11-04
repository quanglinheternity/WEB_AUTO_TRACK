package com.transport.service.vehicleType;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transport.dto.vehicleType.VehicleTypeRequest;
import com.transport.dto.vehicleType.VehicleTypeResponse;
import com.transport.entity.domain.VehicleType;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.VehicleTypeMapper;
import com.transport.repository.vehicleType.VehicleTypeRepository;
import com.transport.util.CodeGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class VehicleTypeServiceImpl implements VehicleTypeService {
    VehicleTypeRepository vehicleTypeRepository;
    VehicleTypeMapper vehicleTypeMapper;
    VehicleTypeValidator vehicleTypeValidator;

    @Override
    public Page<VehicleTypeResponse> search(String keyword, Pageable pageable) {
        Page<VehicleType> page = vehicleTypeRepository.search(keyword, pageable);

        return page.map(vehicleTypeMapper::toResponse);
    }

    @Override
    public VehicleTypeResponse getById(Long id) {
        VehicleType vehicleType = vehicleTypeRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));
        return vehicleTypeMapper.toResponse(vehicleType);
    }

    @Override
    public VehicleTypeResponse create(VehicleTypeRequest request) {
        // Validate trước khi tạo
        vehicleTypeValidator.validateBeforeCreate(request);

        VehicleType vehicleType = vehicleTypeMapper.toEntity(request);
        vehicleType.setCode(CodeGenerator.generateCode("LX"));
        vehicleTypeRepository.save(vehicleType);

        return vehicleTypeMapper.toResponse(vehicleType);
    }

    @Override
    public VehicleTypeResponse update(Long id, VehicleTypeRequest request) {
        VehicleType vehicleType = vehicleTypeRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));

        vehicleTypeValidator.validateBeforeUpdate(id, request);

        vehicleTypeMapper.updateEntityFromRequest(request, vehicleType);
        vehicleTypeRepository.save(vehicleType);

        return vehicleTypeMapper.toResponse(vehicleType);
    }

    @Override
    public void delete(Long id) {
        VehicleType vehicleType = vehicleTypeRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));

        vehicleTypeRepository.delete(vehicleType);
    }
}
