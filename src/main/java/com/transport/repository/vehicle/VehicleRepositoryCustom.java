package com.transport.repository.vehicle;

import java.math.BigDecimal;

public interface VehicleRepositoryCustom {
    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVin(String vin);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    boolean existsByVinAndIdNot(String vin, Long id);

    BigDecimal findMaxPayloadByVehicleId(Long vehicleId);
    
}
