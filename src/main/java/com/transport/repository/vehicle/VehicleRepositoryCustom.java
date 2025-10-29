package com.transport.repository.vehicle;



public interface VehicleRepositoryCustom {
    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVin(String vin);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    boolean existsByVinAndIdNot(String vin, Long id);

    
}
