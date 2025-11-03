package com.transport.repository.vehicle;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleSearchRequest;

public interface VehicleRepositoryCustom {
    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVin(String vin);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    boolean existsByVinAndIdNot(String vin, Long id);

    BigDecimal findMaxPayloadByVehicleId(Long vehicleId);

    Page<VehicleResponse> searchVehicles(VehicleSearchRequest request, Pageable pageable);
    
}
