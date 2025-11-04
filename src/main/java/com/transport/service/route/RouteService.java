package com.transport.service.route;

import org.springframework.data.domain.Pageable;

import com.transport.dto.page.PageResponse;
import com.transport.dto.route.RouteRequest;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteSearchRequest;
import com.transport.dto.route.RouteUpdateRequest;

public interface RouteService {
    PageResponse<RouteResponse> getAll(RouteSearchRequest request, Pageable pageable);

    RouteResponse getById(Long id);

    RouteResponse create(RouteRequest request);

    RouteResponse update(Long id, RouteUpdateRequest request);

    void delete(Long id);
}
