package com.transport.service.route;

import org.springframework.stereotype.Component;

import com.transport.entity.domain.Route;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.route.RouteRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteValidator {
    RouteRepository routeRepository;

    public Route validateRoute(Long routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new AppException(ErrorCode.ROUTE_NOT_FOUND));
        if (!Boolean.TRUE.equals(route.getIsActive())) {
            throw new AppException(ErrorCode.ROUTE_INACTIVE);
        }
        return route;
    }
}
