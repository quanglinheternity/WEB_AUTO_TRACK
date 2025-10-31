package com.transport.service.route;

import java.util.List;

import com.transport.dto.route.RouteRequest;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteUpdateRequest;

public interface RouteService {
    List<RouteResponse> getAll();
    RouteResponse getById(Long id);
    RouteResponse create(RouteRequest request);
    RouteResponse update(Long id, RouteUpdateRequest request);
    void delete(Long id);
}
