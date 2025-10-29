package com.transport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.transport.dto.vehicleType.VehicleTypeRequest;
import com.transport.dto.vehicleType.VehicleTypeResponse;
import com.transport.entity.domain.Vehicle;
import com.transport.entity.domain.VehicleType;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VehicleTypeMapper {

    VehicleTypeMapper INSTANCE = Mappers.getMapper(VehicleTypeMapper.class);

    // Request → Entity
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    @Mapping(target = "code", ignore = true)
    VehicleType toEntity(VehicleTypeRequest request);

    // Entity → Response
    @Mapping(target = "vehicles", source = "vehicles")
    VehicleTypeResponse toResponse(VehicleType entity);

    // Update entity từ request (dùng khi PUT/PATCH)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "code", ignore = true) // không cho sửa code
    void updateEntityFromRequest(VehicleTypeRequest request, @MappingTarget VehicleType entity);

    // Nếu cần map danh sách xe (tùy chọn)
    @Mapping(target = "status", source = "status")
    VehicleTypeResponse.VehicleSummary toVehicleSummary(Vehicle vehicle);
}
