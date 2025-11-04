package com.transport.repository.vehicleType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.VehicleType;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long>, VehicleTypeRepositoryCustom {}
