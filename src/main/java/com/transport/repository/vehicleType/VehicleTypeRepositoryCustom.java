package com.transport.repository.vehicleType;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.entity.domain.VehicleType;

public interface VehicleTypeRepositoryCustom {
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByName(String name);
    Optional<VehicleType> findByIdAndIsActiveTrue(Long id);
    Page<VehicleType> search(String keyword, Pageable pageable);
}
