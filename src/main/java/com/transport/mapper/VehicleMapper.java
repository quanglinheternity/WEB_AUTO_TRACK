package com.transport.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleUpdateRequest;
import com.transport.entity.domain.Vehicle;
import com.transport.enums.VehicleStatus;

@Mapper(
        componentModel = "spring",
        uses = {TripMapper.class})
public interface VehicleMapper {

    @Mapping(target = "vehicleTypeName", source = "vehicleType.name")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusToDescription")
    // @Mapping(source = "route.name", target = "routeName") // Nâng cao
    VehicleResponse toVehicleResponse(Vehicle vehicle);

    @Named("mapStatusToDescription")
    default String mapStatusToDescription(VehicleStatus status) {
        return status != null ? status.getDescription() : null;
    }

    @Mapping(target = "vehicleTypeName", source = "vehicleType.name")
    @Mapping(target = "trips", source = "trips")
    VehicleAndTripResponse toVehicleDetailResponse(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "vehicleType", ignore = true)
    @Mapping(target = "inspectionExpiryDate", source = "inspectionExpiryDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "purchaseDate", source = "purchaseDate", dateFormat = "yyyy-MM-dd")
    Vehicle toVehicleFromCreateRequest(VehicleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "vehicleType", ignore = true)
    @Mapping(target = "trips", ignore = true)
    // @Mapping(target = "purchaseDate", source = "purchaseDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "inspectionExpiryDate", source = "inspectionExpiryDate", dateFormat = "yyyy-MM-dd")
    void updateVehicleFromUpdateRequest(VehicleUpdateRequest request, @MappingTarget Vehicle vehicle);
}
