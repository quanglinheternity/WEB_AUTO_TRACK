package com.transport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.transport.dto.route.RouteRequest;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteUpdateRequest;
import com.transport.entity.domain.Route;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RouteMapper {
    RouteResponse toRouteResponse(Route route);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "trips", ignore = true)
    Route toCreateRoute(RouteRequest routeRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateRouteFromRequest(RouteUpdateRequest request, @MappingTarget Route route);
}
