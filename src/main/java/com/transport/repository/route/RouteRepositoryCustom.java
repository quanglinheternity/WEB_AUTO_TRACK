package com.transport.repository.route;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteSearchRequest;

public interface RouteRepositoryCustom {
    Page<RouteResponse> searchRoutes(RouteSearchRequest request, Pageable pageable);
}
