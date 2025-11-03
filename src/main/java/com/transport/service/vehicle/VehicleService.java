package com.transport.service.vehicle;


import org.springframework.data.domain.Pageable;

import com.transport.dto.page.PageResponse;
import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleSearchRequest;
import com.transport.dto.vehicle.VehicleUpdateRequest;

public interface  VehicleService {
    PageResponse<VehicleResponse> getAll(VehicleSearchRequest request, Pageable pageable);

    VehicleResponse create(VehicleRequest request);

    VehicleResponse update(Long id, VehicleUpdateRequest request);

    VehicleAndTripResponse getById(Long id);

    VehicleResponse toggleActiveStatus(Long id);

    void delete(Long id);
}
