package com.transport.service.vehicle;

import java.util.List;

import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleUpdateRequest;

public interface  VehicleService {
    List<VehicleResponse> getAll();

    VehicleResponse create(VehicleRequest request);

    VehicleResponse update(Long id, VehicleUpdateRequest request);

    VehicleAndTripResponse getById(Long id);

    VehicleResponse toggleActiveStatus(Long id);

    void delete(Long id);
}
