package com.transport.repository.route;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.route.RouteBySalary;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteSearchRequest;

public interface RouteRepositoryCustom {
    Page<RouteResponse> searchRoutes(RouteSearchRequest request, Pageable pageable);

    List<RouteBySalary> findRoutesByDriverAndMonth(Long driverId, YearMonth month);
}
