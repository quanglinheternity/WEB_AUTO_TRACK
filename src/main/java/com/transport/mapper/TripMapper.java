package com.transport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.transport.dto.trip.TripResponse;
import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.entity.domain.Trip;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "routeName", source = "route.name")
    @Mapping(target = "vehiclePlateNumber", source = "vehicle.licensePlate")
    @Mapping(target = "driverName", source = "driver.user.fullName")
    @Mapping(target = "createdBy", expression = "java(trip.getCreatedBy() != null ? trip.getCreatedBy().getFullName() : null)")
    @Mapping(target = "approvedBy", expression = "java(trip.getApprovedByUser() != null ? trip.getApprovedByUser().getFullName() : null)")
    TripResponse toTripResponse(Trip trip);

    @Mapping(source = "route.name", target = "routeName") // ✅ map routeName từ entity Route
    @Mapping(source = "vehicle.licensePlate", target = "vehiclePlateNumber")
    @Mapping(source = "driver.user.fullName", target = "driverName")
    VehicleAndTripResponse.TripResponseSimple toTripResponseSimple(Trip trip);
}
