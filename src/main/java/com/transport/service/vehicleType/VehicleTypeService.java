package com.transport.service.vehicleType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.vehicleType.VehicleTypeRequest;
import com.transport.dto.vehicleType.VehicleTypeResponse;

public interface VehicleTypeService {
    VehicleTypeResponse create(VehicleTypeRequest request);

    VehicleTypeResponse update(Long id, VehicleTypeRequest request);

    void delete(Long id);

    VehicleTypeResponse getById(Long id);

    Page<VehicleTypeResponse> search(String keyword, Pageable pageable);
}
