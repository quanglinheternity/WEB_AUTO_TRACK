package com.transport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.transport.dto.trip.TripCreateRequest;
import com.transport.dto.trip.TripResponse;
import com.transport.dto.trip.TripUpdateRequest;
import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.entity.domain.Trip;
import com.transport.enums.TripStatus;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "routeName", source = "route.name")
    @Mapping(target = "vehiclePlateNumber", source = "vehicle.licensePlate")
    @Mapping(target = "driverName", source = "driver.user.fullName")
    @Mapping(target = "createdBy", expression = "java(trip.getCreatedBy() != null ? trip.getCreatedBy().getFullName() : null)")
    @Mapping(target = "approvedBy", expression = "java(trip.getApprovedByUser() != null ? trip.getApprovedByUser().getFullName() : null)")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusToDescription")
    TripResponse toTripResponse(Trip trip);
    @Named("mapStatusToDescription")
    default String mapStatusToDescription(TripStatus status) {
        return status != null ? status.getDescription() : null;
    }
    @Mapping(source = "route.name", target = "routeName") 
    @Mapping(source = "vehicle.licensePlate", target = "vehiclePlateNumber")
    @Mapping(source = "driver.user.fullName", target = "driverName")
    VehicleAndTripResponse.TripResponseSimple toTripResponseSimple(Trip trip);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "actualArrivalTime", ignore = true)
    @Mapping(target = "approvalStatus", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "expenses", ignore = true)
    @Mapping(target = "approvedByUser", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "tripCode", ignore = true)
    Trip toCreateTrip(TripCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "expenses", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "actualArrivalTime", ignore = true)
    @Mapping(target = "approvalStatus", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "approvedByUser", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "tripCode", ignore = true)
    void updateFromRequest(TripUpdateRequest request, @MappingTarget Trip trip);
}
