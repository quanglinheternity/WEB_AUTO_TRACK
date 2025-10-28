package com.transport.repository.vehicle;

import java.util.Optional;

import com.transport.entity.domain.Vehicle;

public interface VehicleRepositoryCustom {
    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVin(String vin);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    boolean existsByVinAndIdNot(String vin, Long id);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    
    // List<Vehicle> findByActiveStatus(Boolean activeStatus);

    // List<Vehicle> findByMainDriverId(Integer mainDriverId);
}
